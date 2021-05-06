package com.dreampany.pos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dreampany.pos.databinding.FragmentFirstBinding
import com.starmicronics.stario.PortInfo
import com.starmicronics.stario.StarIOPort
import com.starmicronics.stario.StarIOPortException
import com.starmicronics.starioextension.ConnectionCallback
import com.starmicronics.starioextension.ICommandBuilder
import com.starmicronics.starioextension.StarIoExt
import com.starmicronics.starioextension.StarIoExtManager
import kotlinx.coroutines.*
import timber.log.Timber
import java.nio.charset.Charset
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), CoroutineScope {

    companion object {
        val TAG = FirstFragment::class.java.simpleName
    }

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    private var starManager: StarIoExtManager? = null

    @Transient
    private var searchingPorts: Boolean = false

    @Transient
    private val ports: ArrayList<PortInfo> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
        job = Job()

        binding.buttonSearch.setOnClickListener {
            searchPrinters()
        }

        binding.buttonConnect.setOnClickListener {
            connectPrinter()
        }

        binding.buttonPrint.setOnClickListener {
            print()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        _binding = null
    }

    private fun searchPrinters() {
        job.cancel()
        this@FirstFragment.ports.clear()
        launch {
            val ports = searchPorts()
            ports?.forEach {
                Timber.d("Port %s", it)
            }
            ports?.let {
                this@FirstFragment.ports.addAll(it)
            }
        }
    }

    private fun connectPrinter() {
        if (ports.isEmpty()) return
        val port = ports.first()
        if (starManager != null) {
            starManager?.disconnect(object : ConnectionCallback() {
                override fun onDisconnected() {
                    connectPrinter(port)
                }
            })
            return
        }
        connectPrinter(port)
    }

    private fun connectPrinter(port: PortInfo) {
        starManager = StarIoExtManager(
            StarIoExtManager.Type.Standard,
            port.portName,
            ModelCapability.getPortSettings(ModelCapability.SP700),
            10000,
            activity
        )
    }

    private fun print() {
        if (ports.isEmpty()) return
        val port = ports.first()

        val receipt = createReceiptData()
        //Communication.sendCommands(mStarIoExtManager, data, mStarIoExtManager.getPort(), 30000, mCallback);     // 10000mS!!!
    }


    private suspend fun searchPorts(): List<PortInfo>? {
        return withContext(Dispatchers.IO) {
            try {
                StarIOPort.searchPrinter(
                    Constants.IF_TYPE_ETHERNET,
                    activity
                )
            } catch (error: StarIOPortException) {
                Timber.e(error)
                null
            }
        }
    }

    private fun createReceiptData(): ByteArray {
        val builder = StarIoExt.createCommandBuilder(StarIoExt.Emulation.StarDotImpact)

        builder.beginDocument()

        val encoding = Charset.forName("US-ASCII")
        builder.appendCodePage(ICommandBuilder.CodePageType.CP998)

        builder.appendInternational(ICommandBuilder.InternationalType.USA)

        builder.appendCharacterSpace(0)

        builder.appendAlignment(ICommandBuilder.AlignmentPosition.Center)

        builder.append(
            ("Butler\n" +
                    "\n").toByteArray(encoding)
        )


        builder.appendAlignment(ICommandBuilder.AlignmentPosition.Left)

        builder.append(
            (
                    "Description    Total\n" +
                            "Polao          10.99\n" +
                            "\n" +
                            "Subtotal       10.99\n" +
                            "Tax             0.00\n" +
                            "--------------------------------\n").toByteArray(encoding)
        )


        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed)

        builder.endDocument()

        return builder.commands
    }
}