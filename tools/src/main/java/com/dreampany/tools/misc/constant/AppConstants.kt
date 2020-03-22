package com.dreampany.tools.misc.constant

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

    object Times {
        object Crypto {
            val LISTING = TimeUnit.HOURS.toMillis(1)
            val COIN = TimeUnit.MINUTES.toMillis(10)
        }
    }

    object Limit {
        object Crypto {
            const val LIST = 100L
        }
    }

    object ApiKeys {
        object Crypto {
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
        }
    }
}