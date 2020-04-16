package com.dreampany.tools.misc.constant

import android.content.Intent
import com.dreampany.common.misc.constant.Constants
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppConstants {

    object Room {
        const val TYPE_CRYPTO = "crypto"
    }

    object Keys {

        object PrefKeys {
            const val PREF = "pref"
            const val CRYPTO = "crypto"

            object Crypto {
                const val CURRENCY = "crypto-currency"
                const val SORT = "crypto-sort"
                const val ORDER = "crypto-order"
                const val EXPIRE = "crypto-expire"
            }
        }

        object Coin {
            const val CRYPTO = "crypto"
            const val COINS = "coins"
            const val QUOTES = "quotes"

            const val MARKET_PAIRS = "market_pairs"
            const val CIRCULATING_SUPPLY = "circulating_supply"
            const val TOTAL_SUPPLY = "total_supply"
            const val MAX_SUPPLY = "max_supply"
            const val LAST_UPDATED = "last_updated"
            const val DATE_ADDED = "date_added"
        }

        object Quote {
            const val CURRENCY = "currency"
            const val VOLUME_24H = "volume_24h"
            const val MARKET_CAP = "market_cap"
            const val CHANGE_1H = "percent_change_1h"
            const val CHANGE_24H = "percent_change_24h"
            const val CHANGE_7D = "percent_change_7d"
            const val LAST_UPDATED = "last_updated"
        }

        object Trade {
            const val FROM_SYMBOL = "from_symbol"
            const val TO_SYMBOL = "to_symbol"
            const val VOLUME_24H = "volume_24h"
            const val VOLUME_24H_TO = "volume_24h_to"
        }

        object Exchange {
            const val FROM_SYMBOL = "from_symbol"
            const val TO_SYMBOL = "to_symbol"
            const val VOLUME_24H = "volume_24h"
            const val CHANGE_24H = "change_24h"
            const val CHANGE_PCT_24H = "change_pct_24h"
        }
    }

    object Notify {
        const val FOREGROUND_ID = Constants.Notify.FOREGROUND_ID
        const val PLAYER_FOREGROUND_ID = 104
        const val FOREGROUND_CHANNEL_ID = Constants.Notify.FOREGROUND_CHANNEL_ID
        const val PLAYER_FOREGROUND_CHANNEL_ID = "player_" + Constants.Notify.FOREGROUND_CHANNEL_ID
    }

    object Times {
        object Crypto {
            val LISTING = TimeUnit.HOURS.toMillis(1)
            val COIN = TimeUnit.MINUTES.toMillis(10)
        }

        val NOTIFY = TimeUnit.MINUTES.toSeconds(1)
        val SERVER = TimeUnit.DAYS.toMillis(1)
        val STATION = TimeUnit.DAYS.toMillis(10)
        val FIREBASE = TimeUnit.HOURS.toMillis(1)

        object Word {
            val FREQUENT = TimeUnit.MINUTES.toMillis(1)
            val NORMAL = TimeUnit.MINUTES.toMillis(3)
            val LAZY = TimeUnit.MINUTES.toMillis(5)
            val DEAD = TimeUnit.HOURS.toMillis(1)
        }

        fun minuteToMillis(minutes: Long): Long {
            return TimeUnit.MINUTES.toMillis(minutes)
        }
    }

    object Limit {
        object Crypto {
            const val LIST = 10L
        }
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

    object Extension {
        const val M3U8 = ".m3u8"
    }

    object Header {
        const val ICY_METADATA = "Icy-MetaData"
        const val ICY_METADATA_OK = "1"
        const val ACCEPT_ENCODING = "Accept-Encoding"
        const val ACCEPT_ENCODING_IDENTITY = "identity"
    }

    object MimeType {
        const val AUDIO_MPEG = "audio/mpeg"
    }

    object ContentType {
        const val APPLE_MPEGURL = "application/vnd.apple.mpegurl"
        const val X_MPEGURL = "application/x-mpegurl"
    }
}