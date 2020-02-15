package com.dreampany.tools.data.mapper

import com.dreampany.framework.data.enums.Quality
import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.util.NetworkUtil
import com.dreampany.framework.util.TimeUtil
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.api.NoteDataSource
import com.dreampany.tools.data.source.api.ServerDataSource
import com.dreampany.tools.data.source.pref.VpnPref
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.injector.annote.ServerAnnote
import com.dreampany.tools.injector.annote.ServerItemAnnote
import com.dreampany.tools.ui.model.ServerItem
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by roman on 2019-10-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ServerMapper
@Inject constructor(
    @ServerAnnote private val map: SmartMap<String, Server>,
    @ServerAnnote private val cache: SmartCache<String, Server>,
    @ServerItemAnnote private val uiMap: SmartMap<String, ServerItem>,
    @ServerItemAnnote private val uiCache: SmartCache<String, ServerItem>,
    private val pref: VpnPref
) : Mapper() {

    fun setServer(server: Server) {
        pref.setServer(server)
    }

    fun getServer() : Server? {
        return pref.getServer()
    }

    fun isExpired(): Boolean {
        val lastTime = pref.getServerTime()
        return TimeUtilKt.isExpired(lastTime, Constants.Time.SERVER)
    }

    fun commitExpireTime() {
        pref.commitServerTime()
    }

    fun getUiItem(id: String): ServerItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: ServerItem) {
        uiMap.put(id, uiItem)
    }

    fun getItems(body: ResponseBody, tempUrl: String): List<Server>? {
        val written = writeBody(body, tempUrl)
        if (!written) {
            return null
        }
        val result = readLines(tempUrl)
        val servers = arrayListOf<Server>()
        result?.drop(2)?.forEach { data ->
            getItem(data)?.run {
                servers.add(this)
                Timber.v("Server Parsing.. %s", this.toString())
            }
        }
        return servers
    }

    fun getItems(body: ResponseBody, tempUrl: String, limit: Long): List<Server>? {
        val written = writeBody(body, tempUrl)
        if (!written) {
            return null
        }
        val result = readLines(tempUrl)
        val servers = arrayListOf<Server>()
        result?.drop(2)?.forEach { data ->
            if (servers.size == limit.toInt()) {
                return@forEach
            }
            getItem(data)?.run {
                servers.add(this)
                Timber.v("Server Parsing.. %s", this.toString())
            }
        }
        return servers
    }

    fun getItem(input: Store, source: ServerDataSource): Server? {
        var out: Server? = map.get(input.id)
        if (out == null) {
            out = source.getItem(input.id)
            map.put(input.id, out)
        }
        return out
    }

    fun getItem(data: String?): Server? {
        if (data.isNullOrEmpty()) {
            return null
        }
        val parts = data.split(Constants.Delimiter.COMMA)
        if (parts.isNullOrEmpty() || parts.size < Constants.VpnGate.SERVER_PARTS) {
            return null
        }
        val host: String = parts.get(Constants.VpnGate.INDEX_HOST)
        val ip: String = parts.get(Constants.VpnGate.INDEX_IP)
        val score: Long = parts.get(Constants.VpnGate.INDEX_SCORE).toLong()
        val ping: Int = getPingValue(parts.get(Constants.VpnGate.INDEX_PING))
        val speed: Int = parts.get(Constants.VpnGate.INDEX_SPEED).toInt()
        val countryName: String = parts.get(Constants.VpnGate.INDEX_COUNTRY_NAME)
        val countryCode: String = parts.get(Constants.VpnGate.INDEX_COUNTRY_CODE)
        val sessions: Int = parts.get(Constants.VpnGate.INDEX_NUM_VPN_SESSIONS).toInt()
        val uptime: Long = parts.get(Constants.VpnGate.INDEX_UPTIME).toLong()
        val users: Long = parts.get(Constants.VpnGate.INDEX_TOTAL_USERS).toLong()
        val traffic: Long = parts.get(Constants.VpnGate.INDEX_TOTAL_TRAFFIC).toLong()
        val logType = parts.get(Constants.VpnGate.INDEX_LOG_TYPE)
        val operator = parts.get(Constants.VpnGate.INDEX_OPERATOR)
        val message = parts.get(Constants.VpnGate.INDEX_MESSAGE)
        val config = parts.get(Constants.VpnGate.INDEX_CONFIG_DATA)

        val quality: Quality = NetworkUtil.getConnectionQuality(ping, speed, sessions)

        var out: Server? = map.get(ip)
        if (out == null) {
            out = Server(ip)
            map.put(ip, out)
        }
        out.run {
            this.host = host
            this.score = score
            this.ping = ping
            this.ping = ping
            this.speed = speed
            this.countryName = countryName
            this.countryCode = countryCode
            this.sessions = sessions
            this.uptime = uptime
            this.users = users
            this.traffic = traffic
            this.logType = logType
            this.operator = operator
            this.message = message
            this.config = config

            this.quality = quality
        }
        return out
    }

    private fun getPingValue(ping: String): Int {
        try {
            return ping.toInt()
        } catch (error: NumberFormatException) {
            return 0
        }
    }

    private fun writeBody(body: ResponseBody, tempUrl: String): Boolean {
        var success = false
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            val file = File(tempUrl)
            input = body.byteStream()
            output = FileOutputStream(file)
            val array = ByteArray(Constants.File.BYTE_ARRAY_SIZE)

            while (true) {
                val read = input.read(array)
                if (read == -1) {
                    break;
                }
                output.write(array, 0, read);
            }
            output.flush()
            success = true
        } catch (error: Throwable) {
            Timber.e(error)
        } finally {
            input?.run {
                close()
            }
            output?.run {
                close()
            }
        }
        return success
    }

    private fun readLines(tempUrl: String): List<String>? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader(tempUrl))
            return reader.readLines()
        } catch (error: Throwable) {
            Timber.e(error)
        } finally {
            reader?.run {
                close()
            }
        }
        return null
    }
}