package com.dreampany.pos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreampany.pos.databinding.FragmentFirstBinding
import com.starmicronics.stario.PortInfo
import com.starmicronics.stario.StarIOPort
import com.starmicronics.stario.StarIOPortException
import com.starmicronics.starioextension.ICommandBuilder
import com.starmicronics.starioextension.StarIoExt
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

    @Transient
    private var searchingPorts: Boolean = false

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


        binding.buttonFirst.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        _binding = null
    }

    private fun doReady() {
        job.cancel()
        launch {
            val ports = searchPorts()
            ports?.forEach {
                Timber.d("Port %s", it.portName)
            }
        }
    }

    private fun print() {
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
                    "\n").toByteArray(encoding))


        builder.appendAlignment(ICommandBuilder.AlignmentPosition.Left)

        builder.append((
                "Description    Total\n" +
                        "Polao          10.99\n" +
                        "\n" +
                        "Subtotal       10.99\n" +
                        "Tax             0.00\n" +
                        "--------------------------------\n").toByteArray(encoding))


        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed)

        builder.endDocument()

        return builder.commands
    }
}