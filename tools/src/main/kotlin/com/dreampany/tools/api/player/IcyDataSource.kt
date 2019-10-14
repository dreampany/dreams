package com.dreampany.tools.api.player

import android.net.Uri
import android.util.Log
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.MediaUtil
import com.dreampany.tools.api.radio.Mapper
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.api.radio.Stream
import com.dreampany.tools.misc.Constants
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.TransferListener
import okhttp3.*
import okhttp3.internal.Util
import org.apache.commons.lang3.ThreadUtils
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class IcyDataSource(
    val http: OkHttpClient,
    val transferLister: TransferListener,
    val listener: Listener,
    val timeUntilStopReconnecting: Long = 0,
    val delayBetweenReconnection: Long = 0
) : HttpDataSource {

    companion object {
        val DEFAULT_TIME_UNTIL_STOP_RECONNECTING: Long = Constants.Time.minuteToMillis(2)
        val DEFAULT_DELAY_BETWEEN_RECONNECTIONS: Long = Constants.Time.minuteToMillis(0)
    }

    interface Listener {
        fun onConnected()
        fun onConnectionLost()
        fun onConnectionLostIrrecoverably()
        fun onShoutCast(cast: ShoutCast)
        fun onStream(stream: Stream)
        fun onBytesRead(buffer: ByteArray, offset: Int, length: Int)
    }

    private lateinit var spec: DataSpec
    private lateinit var request: Request
    private var body: ResponseBody? = null
    private var headers: Map<String, List<String>>? = null

    private var remainingUntilMetadata: Int = Int.MAX_VALUE
    private val readBuffer = ByteArray(256 * 16)

    private var opened: Boolean = false

    private var cast: ShoutCast? = null
    private var stream: Stream? = null

    override fun setRequestProperty(name: String, value: String) {
    }

    override fun clearAllRequestProperties() {
    }

    override fun open(dataSpec: DataSpec): Long {
        close()
        spec = dataSpec

        val allowGzip = (dataSpec.flags and DataSpec.FLAG_ALLOW_GZIP) != 0
        val url = HttpUrl.parse(dataSpec.uri.toString())
        if (url == null) return -1

        val builder = Request.Builder()
            .url(url)
            .addHeader(Constants.Header.ICY_METADATA, Constants.Header.ICY_METADATA_OK)

        if (!allowGzip) {
            builder.addHeader(
                Constants.Header.ACCEPT_ENCODING,
                Constants.Header.ACCEPT_ENCODING_IDENTITY
            )
        }

        request = builder.build()

        return connect()
    }


    override fun getUri(): Uri? {
        return spec.uri
    }

    override fun getResponseHeaders(): Map<String, List<String>> {
        return headers!!
    }

    override fun clearRequestProperty(name: String) {

    }

    @Throws(HttpDataSource.HttpDataSourceException::class)
    override fun close() {
        if (opened) {
            opened = false
            transferLister.onTransferEnd(this, spec, true)
        }
        if (body != null) {
            Util.closeQuietly(body)
            body = null
        }
    }

    override fun addTransferListener(transferListener: TransferListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Throws(HttpDataSource.HttpDataSourceException::class)
    override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int {
        try {
            val bytesTransferred = readInternal(buffer, offset, readLength)
            transferLister.onBytesTransferred(this, spec, true, bytesTransferred)
            return bytesTransferred
        } catch (error: HttpDataSource.HttpDataSourceException) {
            Timber.e(error)
            listener.onConnectionLost()
            val reconnectStartTime = System.currentTimeMillis()

            while (true) {
                val currentTime = System.currentTimeMillis()

                Timber.v("Reconnecting...")

                try {
                    reconnect()
                    break
                } catch (error: HttpDataSource.HttpDataSourceException) {
                    Timber.e(error)
                    if (!AndroidUtil.sleep(delayBetweenReconnection)) {
                        break
                    }
                }

                if (currentTime - reconnectStartTime > timeUntilStopReconnecting) {
                    listener.onConnectionLostIrrecoverably()
                    throw HttpDataSource.HttpDataSourceException(
                        "Reconnection retry time ended.",
                        spec,
                        HttpDataSource.HttpDataSourceException.TYPE_READ
                    )
                }
            }
        }

        return 0
    }

    @Throws(HttpDataSource.HttpDataSourceException::class)
    private fun connect(): Long {
        val response: Response
        try {
            response = http.newCall(request).execute()
        } catch (e: IOException) {
            throw HttpDataSource.HttpDataSourceException(
                "Unable to connect to " + spec.uri.toString(), e,
                spec, HttpDataSource.HttpDataSourceException.TYPE_OPEN
            )
        }

        if (!response.isSuccessful) {
            val headers = request.headers().toMultimap()
            throw HttpDataSource.InvalidResponseCodeException(response.code(), headers, spec)
        }

        body = response.body()

        if (body == null)
            return -1

        headers = request.headers().toMultimap()
        val contentType = body!!.contentType()
        val type =
            contentType?.toString()?.toLowerCase() ?: MediaUtil.getMimeType(
                spec.uri.toString(),
                Constants.MimeType.AUDIO_MPEG
            )

        if (!HttpDataSource.REJECT_PAYWALL_TYPES.evaluate(type)) {
            close()
            throw HttpDataSource.InvalidContentTypeException(type, spec)
        }

        opened = true

        listener.onConnected()
        transferLister.onTransferStart(this, spec, true)

        if (type == Constants.ContentType.APPLE_MPEGURL || type == Constants.ContentType.X_MPEGURL) {
            return body!!.contentLength()
        } else {
            cast = Mapper.decodeShoutCast(response)
            listener.onShoutCast(cast!!)

            remainingUntilMetadata = Integer.MAX_VALUE
            cast?.run {
                remainingUntilMetadata = metadataOffset
            }
        }

        return body!!.contentLength()
    }

    @Throws(HttpDataSource.HttpDataSourceException::class)
    private fun reconnect() {
        close()
        connect()
        Timber.v("Reconnected successfully!");
    }

    @Throws(HttpDataSource.HttpDataSourceException::class)
    private fun readInternal(buffer: ByteArray, offset: Int, readLength: Int): Int {
        val stream = body!!.byteStream()
        var result = 0
        try {
            val len =
                if (remainingUntilMetadata < readLength) remainingUntilMetadata else readLength
            result = stream.read(buffer, offset, len)
        } catch (error: IOException) {
            Timber.e(error)
            throw HttpDataSource.HttpDataSourceException(
                error,
                spec,
                HttpDataSource.HttpDataSourceException.TYPE_READ
            )
        }

        if (result > 0) {
            listener.onBytesRead(buffer, offset, result)
        }

        if (remainingUntilMetadata == result) {
            try {
                readMetaData(stream)
                remainingUntilMetadata = cast!!.metadataOffset
            } catch (error: IOException) {
                throw HttpDataSource.HttpDataSourceException(
                    error,
                    spec,
                    HttpDataSource.HttpDataSourceException.TYPE_READ
                )
            }

        } else {
            remainingUntilMetadata -= result
        }

        return result
    }

    @Throws(IOException::class)
    private fun readMetaData(stream: InputStream): Int {
        val metadataBytes = stream.read() * 16
        var metadataBytesToRead = metadataBytes
        var readBytesBufferMetadata = 0
        var readBytes: Int

        if (metadataBytes > 0) {
            Arrays.fill(readBuffer, 0.toByte())
            while (true) {
                readBytes = stream.read(readBuffer, readBytesBufferMetadata, metadataBytesToRead)
                if (readBytes == 0) {
                    continue
                }
                if (readBytes < 0) {
                    break
                }
                metadataBytesToRead -= readBytes
                readBytesBufferMetadata += readBytes
                if (metadataBytesToRead <= 0) {
                    val meta = String(readBuffer, 0, metadataBytes, Charsets.UTF_8)

                    Timber.v("METADATA:$meta")

                    val rawMetadata = Mapper.decodeShoutCastMetadata(meta)
                    this.stream = Mapper.decodeStream(rawMetadata)
                    listener.onStream(this.stream!!)

                    Timber.v("META:" + this.stream!!.title)
                    break
                }
            }
        }
        return readBytesBufferMetadata + 1
    }
}