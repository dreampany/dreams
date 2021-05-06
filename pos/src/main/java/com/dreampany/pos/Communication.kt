package com.dreampany.pos

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.starmicronics.stario.StarIOPort
import com.starmicronics.stario.StarIOPortException
import com.starmicronics.stario.StarPrinterStatus
import com.starmicronics.stario.StarResultCode

/**
 * Created by roman on 5/6/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Communication {
    companion object {
        fun sendCommands(
            lock: Any,
            commands: ByteArray,
            port: StarIOPort?,
            endCheckedBlockTimeout: Int,
            callback: SendCallback?
        ) {
            val thread = SendCommandThread(
                lock,
                commands,
                port, endCheckedBlockTimeout,
                callback!!
            )
            thread.start()
        }

        fun getCommunicationResultMessage(communicationResult: CommunicationResult): String {
            val builder = StringBuilder()
            when (communicationResult.result) {
                Result.Success -> builder.append("Success!")
                Result.ErrorOpenPort -> builder.append("Fail to openPort")
                Result.ErrorBeginCheckedBlock -> builder.append("Printer is offline (beginCheckedBlock)")
                Result.ErrorEndCheckedBlock -> builder.append("Printer is offline (endCheckedBlock)")
                Result.ErrorReadPort -> builder.append("Read port error (readPort)")
                Result.ErrorWritePort -> builder.append("Write port error (writePort)")
                else -> builder.append("Unknown error")
            }
            if (communicationResult.result != Result.Success) {
                builder.append("\n\nError code: ")
                builder.append(communicationResult.code)
                if (communicationResult.code == StarResultCode.FAILURE) {
                    builder.append(" (Failed)")
                } else if (communicationResult.code == StarResultCode.FAILURE_IN_USE) {
                    builder.append(" (In use)")
                } else if (communicationResult.code == StarResultCode.FAILURE_PAPER_PRESENT) {
                    builder.append(" (Paper present)")
                }
            }
            return builder.toString()
        }
    }

    enum class Result {
        Success,
        ErrorUnknown,
        ErrorOpenPort,
        ErrorBeginCheckedBlock,
        ErrorEndCheckedBlock,
        ErrorWritePort,
        ErrorReadPort
    }

    class CommunicationResult(result: Result, code: Int) {
        private var mResult: Result = Result.ErrorUnknown
        var code = StarResultCode.FAILURE
        val result: Result get() = mResult

        init {
            mResult = result
            this.code = code
        }
    }

    interface SendCallback {
        fun onStatus(communicationResult: CommunicationResult)
    }
}

internal class SendCommandThread : Thread {
    private val mLock: Any
    private var mCallback: Communication.SendCallback
    private var mCommands: ByteArray
    private var mPort: StarIOPort? = null
    private var mPortName: String? = null
    private var mPortSettings: String? = null
    private var mTimeout = 0
    private var mEndCheckedBlockTimeout: Int
    private var mContext: Context? = null

    constructor(
        lock: Any,
        commands: ByteArray,
        port: StarIOPort?,
        endCheckedBlockTimeout: Int,
        callback: Communication.SendCallback
    ) {
        mCommands = commands
        mPort = port
        mEndCheckedBlockTimeout = endCheckedBlockTimeout
        mCallback = callback
        mLock = lock
    }

    constructor(
        lock: Any,
        commands: ByteArray,
        portName: String?,
        portSettings: String?,
        timeout: Int,
        endCheckedBlockTimeout: Int,
        context: Context?,
        callback: Communication.SendCallback
    ) {
        mCommands = commands
        mPortName = portName
        mPortSettings = portSettings
        mTimeout = timeout
        mEndCheckedBlockTimeout = endCheckedBlockTimeout
        mContext = context
        mCallback = callback
        mLock = lock
    }

    override fun run() {
        var result: Communication.Result = Communication.Result.ErrorOpenPort
        var code = StarResultCode.FAILURE
        synchronized(mLock) {
            try {
                if (mPort == null) {
                    mPort = if (mPortName == null) {
                        resultSendCallback(result, code, mCallback)
                        return
                    } else {
                        StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext)
                    }
                }
                if (mPort == null) {
                    result = Communication.Result.ErrorOpenPort
                    resultSendCallback(result, code, mCallback)
                    return
                }
                var status: StarPrinterStatus
                result = Communication.Result.ErrorBeginCheckedBlock
                status = mPort!!.beginCheckedBlock()
                if (status.offline) {
                    throw StarIOPortException("A printer is offline.")
                }
                result = Communication.Result.ErrorWritePort
                mPort!!.writePort(mCommands, 0, mCommands.size)
                result = Communication.Result.ErrorEndCheckedBlock
                mPort!!.setEndCheckedBlockTimeoutMillis(mEndCheckedBlockTimeout)
                status = mPort!!.endCheckedBlock()
                if (status.coverOpen) {
                    throw StarIOPortException("Printer cover is open")
                } else if (status.receiptPaperEmpty) {
                    throw StarIOPortException("Receipt paper is empty")
                } else if (status.offline) {
                    throw StarIOPortException("Printer is offline")
                }
                result = Communication.Result.Success
                code = StarResultCode.SUCCESS
            } catch (e: StarIOPortException) {
                code = e.errorCode
            }
            if (mPort != null && mPortName != null) {
                try {
                    StarIOPort.releasePort(mPort)
                } catch (e: StarIOPortException) {
                    // Nothing
                }
                mPort = null
            }
            resultSendCallback(result, code, mCallback)
        }
    }

    companion object {
        private fun resultSendCallback(result: Communication.Result, code: Int, callback: Communication.SendCallback?) {
            if (callback != null) {
                val handler = Handler(Looper.getMainLooper())
                handler.post { callback.onStatus(Communication.CommunicationResult(result, code)) }
            }
        }
    }
}

