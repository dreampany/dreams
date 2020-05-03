package com.dreampany.tools.misc.constant

import com.dreampany.framework.misc.constant.Constants

/**
 * Created by roman on 15/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RadioConstants {

    object Apis {
        object Radio {
            const val BASE_URL = "http://www.radio-browser.info/webservice/"
            const val RADIO_BROWSER_STATIONS_OF_COUNTRY =
                "json/stations/bycountrycodeexact/{${Keys.Station.COUNTRY_CODE}}"
            const val RADIO_BROWSER_STATIONS_OF_TRENDS =
                "json/stations/topclick/{${Keys.Station.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_OF_POPULAR =
                "json/stations/topvote/{${Keys.Station.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_SEARCH = "json/stations/search"
        }
    }

    object Keys {

        object PrefKeys {
            const val RADIO = "radio"

            object Station {
                const val ORDER = "order"
                const val REVERSE = "reverse"
                const val HIDE_BROKEN = "hide_broken"
                const val EXPIRE = "expire"
            }
        }

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
            const val ID = Constants.Keys.ID
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
            const val ORDER = "order"
            const val REVERSE = "reverse"
            const val OFFSET = "offset"
            const val LIMIT = "limit"
            const val HIDE_BROKEN = "hidebroken"

            object Remote {
                const val CHANGE_UUID = "changeuuid"
                const val STATION_UUID = "stationuuid"
                const val COUNTRY_CODE = "countrycode"
                const val NEGATIVE_VOTES = "negativevotes"
                const val LAST_CHANGE_TIME = "lastchangetime"
                const val LAST_CHECK_OK = "lastcheckok"
                const val LAST_CHECK_TIME = "lastchecktime"
                const val LAST_CHECK_OK_TIME = "lastcheckoktime"
                const val CLICK_TIMESTAMP = "clicktimestamp"
                const val CLICK_COUNT = "clickcount"
                const val CLICK_TREND = "clicktrend"
            }

            object Order {
                const val NAME = "name"
                const val CLICK_COUNT = "clickcount"
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