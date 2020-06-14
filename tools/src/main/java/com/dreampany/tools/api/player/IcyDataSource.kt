package com.dreampany.tools.api.player

import android.net.Uri
import com.dreampany.framework.misc.util.MediaUtil
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.api.radio.Mapper
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.api.radio.Stream
import com.dreampany.tools.misc.constants.AppConstants
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.TransferListener
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.closeQuietly
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
    val transferListener: TransferListener,
    val listener: Listener,
    val retryTimeout: Long = 0,
    val retryDelay: Long = 0
) : HttpDataSource {

    companion object {
        val DEFAULT_TIME_UNTIL_STOP_RECONNECTING: Long = AppConstants.Times.minuteToMillis(2)
        val DEFAULT_DELAY_BETWEEN_RECONNECTIONS: Long = AppConstants.Times.minuteToMillis(0)
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
        val url =  dataSpec.uri.toString().toHttpUrlOrNull()
        if (url == null) return -1

        val builder = Request.Builder()
            .url(url)
            .addHeader(AppConstants.Header.ICY_METADATA, AppConstants.Header.ICY_METADATA_OK)

        if (!allowGzip) {
            builder.addHeader(
                AppConstants.Header.ACCEPT_ENCODING,
                AppConstants.Header.ACCEPT_ENCODING_IDENTITY
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
            transferListener.onTransferEnd(this, spec, true)
        }
        body?.closeQuietly()
        body = null
    }

    override fun addTransferListener(transferListener: TransferListener) {
    }

    @Throws(HttpDataSource.HttpDataSourceException::class)
    override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int {
        try {
            val bytesTransferred = readInternal(buffer, offset, readLength)
            transferListener.onBytesTransferred(this, spec, true, bytesTransferred)
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
                    if (!Util.sleep(retryDelay)) {
                        break
                    }
                }

                if (currentTime - reconnectStartTime > retryTimeout) {
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
            val headers = request.headers.toMultimap()
            throw HttpDataSource.InvalidResponseCodeException(response.code, headers, spec)
        }

        body = response.body

        if (body == null)
            return -1

        headers = request.headers.toMultimap()
        val contentType = body!!.contentType()
        val type =
            contentType?.toString()?.toLowerCase() ?: MediaUtil.getMimeType(
                spec.uri.toString(),
                AppConstants.MimeType.AUDIO_MPEG
            )

        if (!HttpDataSource.REJECT_PAYWALL_TYPES.evaluate(type)) {
            close()
            throw HttpDataSource.InvalidContentTypeException(type, spec)
        }

        opened = true

        listener.onConnected()
        transferListener.onTransferStart(this, spec, true)

        if (type == AppConstants.ContentType.APPLE_MPEGURL || type == AppConstants.ContentType.X_MPEGURL) {
            return body!!.contentLength()
        } else {
            remainingUntilMetadata = Integer.MAX_VALUE
            cast = Mapper.decodeShoutCast(response)
            cast?.run {
                listener.onShoutCast(cast!!)
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
                    this.stream?.run {
                        listener.onStream(this)
                        Timber.v("Stream META:" + this.title)
                    }
                    break
                }
            }
        }
        return readBytesBufferMetadata + 1
    }
}