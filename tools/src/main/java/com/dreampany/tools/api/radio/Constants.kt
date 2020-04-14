package com.dreampany.tools.api.radio

import java.util.*

/**
 * Created by roman on 14/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Default {
        val NULL = null
        const val BOOLEAN = false
        const val CHARACTER = 0.toChar()
        const val INT = 0
        const val LONG = 0L
        const val FLOAT = 0f
        const val DOUBLE = 0.0
        const val STRING = ""
        val LIST = Collections.emptyList<Any>()
    }

    object Sep {
        const val LEAF_SEPARATOR = '|'
        const val EQUAL = '='
        const val SEMI_COLON = ';'
        const val SPACE_HYPHEN_SPACE = " - "
    }

    object Keys {

        object StationKeys {
            const val CHANGE_UUID = "change_uuid"
            const val STATION_UUID = "station_uuid"
            const val COUNTRY_CODE = "country_code"
            const val NEGATIVE_VOTES = "negative_votes"
            const val LAST_CHANGE_TIME = "last_change_time"
            const val LAST_CHECK_OK = "last_check_ok"
            const val LAST_CHECK_TIME = "last_check_time"
            const val LAST_CHECK_OK_TIME = "last_check_ok_time"
            const val CLICK_TIMESTAMP = "click_timestamp"
            const val CLICK_COUNT = "click_count"
            const val CLICK_TREND = "click_trend"
            const val LIMIT = "limit"
        }
        object StreamKeys {
            const val TITLE = "StreamTitle"
        }

        object ShoutCastKeys {
            const val ICY_META_INT = "icy-metaint"
            const val ICY_BR = "icy-br"
            const val ICY_AUDIO_INFO = "ice-audio-info"
            const val ICY_DESCRIPTION = "icy-description"
            const val ICY_GENRE = "icy-genre"
            const val ICY_NAME = "icy-name"
            const val ICY_URL = "icy-url"
            const val SERVER = "Server"
            const val PUBLIC = "icy-pub"
            const val ICY_CHANNELS = "ice-channels"
            const val CHANNELS = "channels"
            const val ICY_SAMPLE_RATE = "ice-samplerate"
            const val SAMPLE_RATE = "samplerate"
            const val ICY_BIT_RATE = "ice-bitrate"
            const val BIT_RATE = "bitrate"
        }
    }

    object Api {

        object Radio {
            const val RADIO_BROWSER = "http://www.radio-browser.info/webservice/"
            const val RADIO_BROWSER_STATIONS_OF_COUNTRY =
                "json/stations/bycountrycodeexact/{${Keys.StationKeys.COUNTRY_CODE}}"
            const val RADIO_BROWSER_STATIONS_OF_TRENDS =
                "json/stations/topclick/{${Keys.StationKeys.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_OF_POPULAR =
                "json/stations/topvote/{${Keys.StationKeys.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_SEARCH = "json/stations/search"
        }
    }
}