package com.dreampany.tools.api.player

import android.net.Uri
import com.dreampany.framework.util.MediaUtil
import com.dreampany.tools.api.radio.Mapper
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.misc.Constants
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.TransferListener
import okhttp3.*
import java.io.IOException

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
        fun onShoutCast(cast: ShoutCast?)
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

    override fun setRequestProperty(name: String?, value: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearAllRequestProperties() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResponseHeaders(): MutableMap<String, MutableList<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearRequestProperty(name: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addTransferListener(transferListener: TransferListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(buffer: ByteArray?, offset: Int, readLength: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            listener.onShoutCast(cast)

            remainingUntilMetadata = Integer.MAX_VALUE
            cast?.run {
                remainingUntilMetadata = metadataOffset
            }
        }

        return body!!.contentLength()
    }
}