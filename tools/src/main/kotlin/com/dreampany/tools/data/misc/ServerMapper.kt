package com.dreampany.tools.data.misc

import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.misc.ServerAnnote
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ServerMapper(
    @ServerAnnote private val map: SmartMap<String, Server>,
    @ServerAnnote private val cache: SmartCache<String, Server>
) : Mapper() {

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
        val ping: Int = parts.get(Constants.VpnGate.INDEX_PING).toInt()
        val speed: Long = parts.get(Constants.VpnGate.INDEX_SPEED).toLong()
        val countryName: String = parts.get(Constants.VpnGate.INDEX_COUNTRY_NAME)
        val countryCode: String = parts.get(Constants.VpnGate.INDEX_COUNTRY_CODE)
        val sessions: Long = parts.get(Constants.VpnGate.INDEX_NUM_VPN_SESSIONS).toLong()
        val uptime: Long = parts.get(Constants.VpnGate.INDEX_UPTIME).toLong()
        val users: Long = parts.get(Constants.VpnGate.INDEX_TOTAL_USERS).toLong()
        val traffic: Long = parts.get(Constants.VpnGate.INDEX_TOTAL_TRAFFIC).toLong()
        val logType = parts.get(Constants.VpnGate.INDEX_LOG_TYPE)
        val operator = parts.get(Constants.VpnGate.INDEX_OPERATOR)
        val message = parts.get(Constants.VpnGate.INDEX_MESSAGE)
        val config = parts.get(Constants.VpnGate.INDEX_CONFIG_DATA)

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
        }
        return out
    }
}