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
import com.starmicronics.starioextension.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.nio.charset.Charset
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {
        val TAG = FirstFragment::class.java.simpleName
    }

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val scope = MainScope()

    private var starManager: StarIoExtManager? = null

    @Transient
    private var searchingPorts: Boolean = false

    @Transient
    private val ports: ArrayList<PortInfo> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        scope.cancel()
        _binding = null
    }

    private fun searchPrinters() {
        this@FirstFragment.ports.clear()
        scope.launch {
            Timber.d("Searching Printers")
            val ports = searchPorts()
            ports?.forEach {
                Timber.d("Port %s", it)
            }
            ports?.let {
                this@FirstFragment.ports.addAll(it)
            }

            if (ports.isNullOrEmpty()) {
                binding.buttonConnect.isEnabled = false
                binding.textStatus.text = "Printer NotFound"
            } else {
                binding.buttonConnect.isEnabled = true
                binding.textStatus.text = ports.firstOrNull()?.portName
            }
        }
    }

    private fun connectPrinter() {
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
        starManager?.setListener(object : StarIoExtManagerListener() {
            override fun onPrinterOnline() {
                binding.textStatus.text = "Printer Online"
            }

            override fun onPrinterOffline() {
                binding.textStatus.text = "Printer Offline"
            }

            override fun onPrinterImpossible() {
                binding.textStatus.text = "Printer Impossible"
            }
        })
        starManager?.connect(object : ConnectionCallback() {
            override fun onConnected(result: Boolean, resultCode: Int) {
                binding.textStatus.text = "Printer Connected"
                binding.buttonPrint.isEnabled = result
            }
        })
    }

    private fun print() {
        val receipt = createReceiptData()
        starManager?.let {
            Communication.sendCommands(
                it,
                receipt,
                it.getPort(),
                30000,
                object : Communication.SendCallback {
                    override fun onStatus(communicationResult: Communication.CommunicationResult) {
                        binding.textStatus.text =
                            Communication.getCommunicationResultMessage(communicationResult)
                    }
                })
        }
    }


    private suspend fun searchPorts(): List<PortInfo>? {
        return withContext(Dispatchers.IO) {
            try {
                val ports = StarIOPort.searchPrinter(
                    Constants.IF_TYPE_ETHERNET
                )
                ports
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
                            "c\n").toByteArray(encoding)
        )


        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed)

        builder.endDocument()

        return builder.commands
    }
}