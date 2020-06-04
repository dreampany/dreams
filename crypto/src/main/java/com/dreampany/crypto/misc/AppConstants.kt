package com.dreampany.crypto.misc

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 29/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppConstants {
    object Keys {

        object Pref {
            const val PREF = "pref"
            const val EXPIRE = "expire"
            const val CURRENCY = "currency"
            const val SORT = "sort"
            const val ORDER = "order"
        }

        object Room {
            const val TYPE_CRYPTO = "crypto"
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

    object Limits {
        const val COINS = 100L
        const val TRADES = 10L
        const val EXCHANGES = 10L
        const val MAX_COINS = 5000L
    }

    object Times {
        object Crypto {
            val LISTING = TimeUnit.MINUTES.toMillis(30)
            val COIN = TimeUnit.MINUTES.toMillis(5)
            val WORKER = TimeUnit.HOURS.toMillis(1)
        }
    }
}