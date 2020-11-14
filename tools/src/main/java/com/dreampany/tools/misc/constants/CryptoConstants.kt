package com.dreampany.tools.misc.constants

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CryptoConstants {
/*    object CryptoCompare {


        const val API_KEY_ROMAN_BJIT = "99cb2ed664b75035fe73b7f93d2e1e949c57f17f23f092260debf93ce1315c2d" //roman.bjit@gmail.com

        const val BASE_URL = "https://min-api.cryptocompare.com/data/"
        const val TRADES = "top/pairs"
        const val EXCHANGES = "top/exchanges/full"

        const val EXTRA_PARAMS = "extraParams"
        const val FROM_SYMBOL = "fsym"
        const val TO_SYMBOL = "tsym"

    }

    object Gecko {
        const val ACCEPT = "Accept"
        const val ACCEPT_ENCODING = "Accept-Encoding"

        const val ACCEPT_JSON = "application/json"
        const val ACCEPT_ZIP = "deflate, gzip"

        const val BASE_URL = "https://api.coingecko.com/api/v3/"


        const val TICKERS  = "coins/{id}/tickers"
        const val ID  = "id"
        const val INCLUDE_IMAGE  = "include_exchange_logo"
    }*/

    object Keys {
        object PrefKeys {
            const val CRYPTO = "crypto"

            object Crypto {
                const val CURRENCY = "crypto-currency"
                const val SORT = "crypto-sort"
                const val ORDER = "crypto-order"
                const val EXPIRE = "crypto-expire"
            }
        }

        object Room {
            const val TYPE_CRYPTO = "crypto"
        }
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

    object Trade {
        const val DATA = "Data"
        const val EXCHANGE = "exchange"
        const val FROM_SYMBOL = "fromSymbol"
        const val TO_SYMBOL = "toSymbol"
        const val VOLUME_24H = "volume24h"
        const val VOLUME_24H_TO = "volume24hTo"
    }

/*    object Exchange {
        const val DATA = "Data"
        const val EXCHANGES = "Exchanges"
        const val MARKET = "MARKET"
        const val FROM_SYMBOL = "FROMSYMBOL"
        const val TO_SYMBOL = "TOSYMBOL"
        const val PRICE = "PRICE"
        const val VOLUME_24H = "VOLUME24HOUR"
        const val CHANGE_24H = "CHANGE24HOUR"
        const val CHANGE_PCT_24H = "CHANGEPCT24HOUR"
    }*/

/*    object Limits {
        const val COINS = 100L
        const val TRADES = 10L
        const val EXCHANGES = 10L
        const val MAX_COINS = 5000L
    }*/

    object Times {
        object Crypto {
            val LISTING = TimeUnit.MINUTES.toMillis(30)
            val COIN = TimeUnit.MINUTES.toMillis(5)
            val WORKER = TimeUnit.HOURS.toMillis(1)
        }
    }
}