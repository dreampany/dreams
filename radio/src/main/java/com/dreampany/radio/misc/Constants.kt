package com.dreampany.radio.misc

import android.content.Intent
import com.dreampany.framework.misc.constant.Constant
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 29/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Keys {

        object Pref {
            const val PREF = "pref"
            const val MISC = "misc"
            const val EXPIRE = "expire"
            const val CATEGORY = "category"
            const val PAGE = "page"
            const val CATEGORIES = "categories"
            const val PAGES = "pages"
            const val SEARCH = "search"

            const val ORDER = "order"
            const val REVERSE = "reverse"
            const val HIDE_BROKEN = "hide_broken"
        }

        object Room {
            const val ROOM = "room"
            const val MISC = "misc"
        }

        object Related {
            const val LEFTER = "lefter"
            const val RIGHTER = "righter"
        }

        object Station {
            const val CHANGE_UUID = "change_uuid"
            const val STATION_UUID = "station_uuid"
            const val URL_RESOLVED = "url_resolved"
            const val COUNTRY_CODE = "country_code"
            const val NEGATIVE_VOTES = "negative_votes"
            const val LAST_CHANGE_TIME = "last_change_time"
            const val LAST_CHECK_OK = "last_check_ok"
            const val LAST_CHECK_TIME = "last_check_time"
            const val LAST_CHECK_OK_TIME = "last_check_ok_time"
            const val LAST_LOCAL_CHECK_TIME = "last_local_check_time"
            const val CLICK_TIMESTAMP = "click_timestamp"
            const val CLICK_COUNT = "click_count"
            const val CLICK_TREND = "click_trend"
            const val ORDER = "order"
            const val REVERSE = "reverse"
            const val OFFSET = "offset"
            const val LIMIT = "limit"
            const val HIDE_BROKEN = "hidebroken"

            object Order {
                const val NAME = "name"
                const val VOTES = "votes"
                const val CLICK_COUNT = "clickcount"
            }
        }
    }

    object Values {
        const val STATIONS = "radio.stations"
        const val SEARCH = "radio.search"
    }

    object Limit {
        const val STATIONS = 100L
    }

    object Times {
        val HOUSE_ADS = TimeUnit.DAYS.toMillis(1)
        val PAGES = TimeUnit.DAYS.toMillis(7)
        val STATIONS = TimeUnit.DAYS.toMillis(1)
    }

    object Count {
        const val MIN_PAGES = 3
    }

    object Service {
        const val VPN_ADDRESS = "10.0.0.2"
        const val VPN_ROUTE = "0.0.0.0"

        const val PLAYER_SERVICE_STATE_CHANGE = "radio_player_state_change"
        const val PLAYER_SERVICE_STATE = "radio_player_state"
        const val PLAYER_SERVICE_UPDATE = "radio_player_state"

        object Command {
            const val START = "start"
            const val RESUME = "resume"
            const val PAUSE = "pause"
            const val STOP = "stop"
            const val NEXT = "next"
            const val PREVIOUS = "previous"
            const val MEDIA_BUTTON = Intent.ACTION_MEDIA_BUTTON
            const val START_LOCK = "start_lock"
            const val STOP_LOCK = "stop_lock"
        }
    }

    object Notify {
        const val PLAYER_FOREGROUND_ID = 104
        const val PLAYER_FOREGROUND_CHANNEL_ID = "player_" + Constant.Notify.FOREGROUND_CHANNEL_ID
    }
}