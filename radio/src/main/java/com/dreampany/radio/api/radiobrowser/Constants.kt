package com.dreampany.radio.api.radiobrowser


/**
 * Created by roman on 31/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Apis {
        object Radio {
            const val BASE_URL = "http://de1.api.radio-browser.info/"
            const val COUNTRY_CODE_EXACT =
                "json/stations/bycountrycodeexact/{${Keys.Station.COUNTRY_CODE}}"
            const val LANGUAGE =
                "json/stations/bylanguageexact/{${Keys.Station.COUNTRY_CODE}}"
            const val TOP_CLICK = "json/stations/topclick/{${Keys.Station.LIMIT}}"
            const val TOP_VOTE = "json/stations/topvote/{${Keys.Station.LIMIT}}"
            const val LAST_CLICK = "json/stations/lastclick/{${Keys.Station.LIMIT}}"
            const val LAST_CHANGE = "json/stations/lastchange/{${Keys.Station.LIMIT}}"
            const val SEARCH = "json/stations/search"
        }
    }

    object Keys {

        object Radio {
            const val STATION_STATE = "station_state"
            const val STATION_TIME = "station_time"

            const val STATION_ID = "station_id"
            const val PLAY_BY_STATION_ID = "play_by_station_id"
            const val PLAY_BY_STATION_UUID = "play_by_station_uuid"

            const val FULL_VOLUME = 100f
            const val DUCK_VOLUME = 40f
        }

        object Station {
            const val COUNTRY_CODE = "country_code"
            const val ORDER = "order"
            const val REVERSE = "reverse"
            const val OFFSET = "offset"
            const val LIMIT = "limit"
            const val HIDE_BROKEN = "hidebroken"

            object Remote {
                const val CHANGE_UUID = "changeuuid"
                const val STATION_UUID = "stationuuid"
                const val URL_RESOLVED = "url_resolved"
                const val COUNTRY_CODE = "countrycode"
                const val NEGATIVE_VOTES = "negativevotes"
                const val LAST_CHANGE_TIME = "lastchangetime"
                const val LAST_CHECK_OK = "lastcheckok"
                const val LAST_CHECK_TIME = "lastchecktime"
                const val LAST_CHECK_OK_TIME = "lastcheckoktime"
                const val LAST_LOCAL_CHECK_TIME = "lastlocalchecktime"
                const val CLICK_TIMESTAMP = "clicktimestamp"
                const val CLICK_COUNT = "clickcount"
                const val CLICK_TREND = "clicktrend"
            }
        }


        object Stream {
            const val TITLE = "StreamTitle"
        }

        object ShoutCast {
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

    object Limits {
        const val STATIONS = 100L
    }
}