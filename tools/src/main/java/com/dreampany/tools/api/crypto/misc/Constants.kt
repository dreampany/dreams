package com.dreampany.tools.api.crypto.misc

/**
 * Created by roman on 9/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Apis {
        object CoinMarketCap {
            const val CMC_PRO_DREAM_DEBUG_2 =
                "24532bfc-8802-4e18-937f-9b682c13df01" //dream.debug.2@gmail.com
            const val CMC_PRO_DREAM_DEBUG_1 =
                "b1334b04-d6ee-4010-866c-aea715bb2d6f" //dream.debug.1@gmail.com
            const val CMC_PRO_ROMAN_BJIT = "2526f063-e73d-4fb9-b221-2bd8c8097525" //roman.bjit@gmail.com
            const val CMC_PRO_IFTE_NET = "e5c34607-689c-4530-886e-0d62c923797a" //ifte.net@gmail.com
            const val CMC_PRO_DREAMPANY =
                "d158ff45-ef74-4562-8984-8d717f422df8" //dreampanymail@gmail.com
            const val CMC_SANDBOX = "ba266b8e-abf4-466f-84cd-38700d6eb8f0"

            // api
            const val BASE_URL = "https://pro-api.coinmarketcap.com/v1/"
            const val BASE_GRAPH_URL = "https://pro-api.coinmarketcap.com/v1/"
            const val IMAGE_URL = "https://s2.coinmarketcap.com/static/img/coins/64x64/%s.png" //id

            const val COINS = "cryptocurrency/listings/latest"
            const val QUOTES = "cryptocurrency/quotes/latest"

            // keys
            const val ACCEPT = "Accept"
            const val ACCEPT_ENCODING = "Accept-Encoding"

            const val ACCEPT_JSON = "application/json"
            const val ACCEPT_ZIP = "deflate, gzip"

            const val API_KEY = "X-CMC_PRO_API_KEY"
            const val CONVERT = "convert"
            const val SORT = "sort"
            const val SORT_DIRECTION = "sort_dir"
            const val AUXILIARIES = "aux"
            const val ID = "id"
        }
    }

    object Keys {
        object Common {
            const val START = "start"
            const val LIMIT = "limit"
            const val STATUS = "status"
            const val DATA = "data"
        }
    }
}