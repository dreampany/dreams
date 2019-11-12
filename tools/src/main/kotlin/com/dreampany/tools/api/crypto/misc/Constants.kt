package com.dreampany.tools.api.crypto.misc

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object CoinMarketCap {
        const val BASE_URL = "https://pro-api.coinmarketcap.com/v1"
        const val LISTING = "/cryptocurrency/listings/latest"

        const val API_KEY = "CMC_PRO_API_KEY"
        const val CONVERT = "convert"


    }

    object Common {
        const val START = "start"
        const val LIMIT = "limit"
        const val STATUS = "status"
        const val DATA = "data"
    }

    object Status {
        const val ERROR_CODE = "error_code"
        const val ERROR_MESSAGE = "error_message"
        const val CREDIT_COUNT = "credit_count"
    }

    object Quote {
        const val VOLUME_24H = "volume_24h"
        const val MARKET_CAP = "market_cap"
        const val CHANGE_1H = "percent_change_1h"
        const val CHANGE_24H = "percent_change_24h"
        const val CHANGE_7D = "percent_change_7d"
        const val LAST_UPDATED = "last_updated"
    }

    object Coin {
        const val CIRCULATING_SUPPLY = "circulating_supply"
        const val TOTAL_SUPPLY = "total_supply"
        const val MAX_SUPPLY = "max_supply"
        const val MARKET_PAIRS = "num_market_pairs"
        const val RANK = "cmc_rank"
        const val QUOTE = "quote"
        const val LAST_UPDATED = "last_updated"
        const val DATE_ADDED = "date_added"
    }
}