package com.dreampany.tools.misc.constants

import android.content.Intent
import com.dreampany.framework.misc.constant.Constant
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    companion object {
        const val APPS_URL =
            "https://firebasestorage.googleapis.com/v0/b/dream-pany.appspot.com/o/apps.json?alt=media&token=f3cdc910-9504-4c89-9d96-bcc651f837a4"
    }

    object Apis {
        object CoinMarketCap {
            const val CMC_PRO_DREAM_DEBUG_2 =
                "24532bfc-8802-4e18-937f-9b682c13df01" //dream.debug.2@gmail.com
            const val CMC_PRO_DREAM_DEBUG_1 =
                "b1334b04-d6ee-4010-866c-aea715bb2d6f" //dream.debug.1@gmail.com
            const val CMC_PRO_ROMAN_BJIT =
                "2526f063-e73d-4fb9-b221-2bd8c8097525" //roman.bjit@gmail.com
            const val CMC_PRO_IFTE_NET = "e5c34607-689c-4530-886e-0d62c923797a" //ifte.net@gmail.com
            const val CMC_PRO_DREAMPANY =
                "d158ff45-ef74-4562-8984-8d717f422df8" //dreampanymail@gmail.com
            const val CMC_SANDBOX = "ba266b8e-abf4-466f-84cd-38700d6eb8f0"

            const val ACCEPT = "Accept"
            const val ACCEPT_ENCODING = "Accept-Encoding"
            const val ACCEPT_JSON = "application/json"
            const val ACCEPT_ZIP = "deflate, gzip"

            const val API_KEY = "X-CMC_PRO_API_KEY"
            const val IMAGE_URL = "https://s2.coinmarketcap.com/static/img/coins/64x64/%s.png" //id
        }

        object CryptoCompare {
            const val API_KEY_ROMAN_BJIT = "99cb2ed664b75035fe73b7f93d2e1e949c57f17f23f092260debf93ce1315c2d" //roman.bjit@gmail.com

            const val ACCEPT = "Accept"
            const val ACCEPT_ENCODING = "Accept-Encoding"

            const val ACCEPT_JSON = "application/json"
            const val ACCEPT_ZIP = "deflate, gzip"

            const val AUTHORIZATION = "authorization"

            const val TICKERS  = "coins/{id}/tickers"
            const val ID  = "id"
            const val INCLUDE_IMAGE  = "include_exchange_logo"
        }

        object Gecko {
            const val ACCEPT = "Accept"
            const val ACCEPT_ENCODING = "Accept-Encoding"

            const val ACCEPT_JSON = "application/json"
            const val ACCEPT_ZIP = "deflate, gzip"

            const val TICKERS  = "coins/{id}/tickers"
            const val ID  = "id"
            const val INCLUDE_IMAGE  = "include_exchange_logo"
        }
    }

    object Keys {
        object Pref {
            const val PREF = "pref"
            const val MISC = "misc"
            const val EXPIRE = "expire"

            object Crypto {
                const val PREF = "crypto.pref"
            }

            object Radio {
                const val PREF = "radio.pref"

                const val PAGE = "radio.page"
                const val PAGES = "radio.pages"
            }

            object News {
                const val CATEGORY = "news.category"

                /*const val CATEGORIES = "news.categories"*/
                const val PAGE = "news.page"
                const val PAGES = "news.pages"
            }
        }

        object Room {
            const val MISC = "misc"
            const val RADIO = "radio"
        }

        object Station {
            const val CHANGE_UUID = "change_uuid"
            const val STATION_UUID = "station_uuid"
            const val URL_RESOLVED = "url_resolved"
            const val COUNTRY_CODE = "country_code"
            const val LAST_CHANGE_TIME = "last_change_time"
            const val LAST_CHECK_OK = "last_check_ok"
            const val LAST_CHECK_TIME = "last_check_time"
            const val LAST_CHECK_OK_TIME = "last_check_ok_time"
            const val LAST_LOCAL_CHECK_TIME = "last_local_check_time"
            const val CLICK_TIMESTAMP = "click_timestamp"
            const val CLICK_COUNT = "click_count"
            const val CLICK_TREND = "click_trend"
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

    object Values {
        object News {
            const val ARTICLES = "tools.news.articles"
            const val SEARCH = "tools.news.search"
        }

        object Radio {
            const val STATIONS = "tools.radio.stations"
            const val SEARCH = "tools.radio.search"
        }

    }

    object Notify {
        const val PLAYER_FOREGROUND_ID = 104
        const val PLAYER_FOREGROUND_CHANNEL_ID = "player_" + Constant.Notify.FOREGROUND_CHANNEL_ID
    }

    object Times {
        val HOUSE_ADS = TimeUnit.DAYS.toMillis(1)
        val NOTIFY = TimeUnit.MINUTES.toSeconds(1)
        val SERVER = TimeUnit.DAYS.toMillis(1)
        val FIREBASE = TimeUnit.HOURS.toMillis(1)

        object Crypto {
            val COINS = TimeUnit.MINUTES.toMillis(30)
            val COIN = TimeUnit.MINUTES.toMillis(5)
            val WORKER = TimeUnit.HOURS.toMillis(1)
        }

        object Radio {
            val STATION = TimeUnit.DAYS.toMillis(10)
            val STATIONS = TimeUnit.DAYS.toMillis(1)
        }

        object Word {
            val FREQUENT = TimeUnit.MINUTES.toMillis(1)
            val NORMAL = TimeUnit.MINUTES.toMillis(3)
            val LAZY = TimeUnit.MINUTES.toMillis(5)
            val DEAD = TimeUnit.HOURS.toMillis(1)
        }

        object News {
            val CATEGORIES = TimeUnit.DAYS.toMillis(7)
        }

        fun minuteToMillis(minutes: Long): Long {
            return TimeUnit.MINUTES.toMillis(minutes)
        }
    }

    object Count {
        object Radio {
            const val MIN_PAGES = 3
        }
        object News {
            const val MIN_PAGES = 3
        }
    }

    object Limits {
        const val STATIONS = 100L
        const val COINS = 100L
        const val TRADES = 10L
        const val EXCHANGES = 10L
        const val MAX_COINS = 5000L
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