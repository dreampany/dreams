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
            // api
            const val BASE_URL = "https://pro-api.coinmarketcap.com/v1/"
            const val BASE_GRAPH_URL = "https://graphs2.coinmarketcap.com/"
            const val IMAGE_URL = "https://s2.coinmarketcap.com/static/img/coins/64x64/%s.png" //id

            const val COINS = "cryptocurrency/listings/latest"
            const val QUOTES = "cryptocurrency/quotes/latest"
            const val GRAPH =
                "currencies/{slug}/{${Keys.CoinMarketCap.START_TIME}}/{${Keys.CoinMarketCap.END_TIME}}"

            // keys
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

        object CoinMarketCap {
            const val SLUG = "slug"
            const val START_TIME = "start_time"
            const val END_TIME = "end_time"
            const val PRICE_BTC = "price_btc"
            const val PRICE_USD = "price_usd"
            const val VOLUME_USD = "volume_usd"
        }
    }
}