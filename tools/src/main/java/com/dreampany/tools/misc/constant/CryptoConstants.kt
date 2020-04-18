package com.dreampany.tools.misc.constant

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CryptoConstants {

    object CoinMarketCap {
        const val ACCEPT = "Accept"
        const val ACCEPT_ENCODING = "Accept-Encoding"

        const val ACCEPT_JSON = "application/json"
        const val ACCEPT_ZIP = "deflate, gzip"

        const val CMC_PRO_DREAM_DEBUG_2 =
            "24532bfc-8802-4e18-937f-9b682c13df01" //dream.debug.2@gmail.com
        const val CMC_PRO_DREAM_DEBUG_1 =
            "b1334b04-d6ee-4010-866c-aea715bb2d6f" //dream.debug.1@gmail.com
        const val CMC_PRO_ROMAN_BJIT = "2526f063-e73d-4fb9-b221-2bd8c8097525" //roman.bjit@gmail.com
        const val CMC_PRO_IFTE_NET = "e5c34607-689c-4530-886e-0d62c923797a" //ifte.net@gmail.com
        const val CMC_PRO_DREAMPANY =
            "d158ff45-ef74-4562-8984-8d717f422df8" //dreampanymail@gmail.com
        const val CMC_SANDBOX = "ba266b8e-abf4-466f-84cd-38700d6eb8f0"

        const val BASE_URL = "https://pro-api.coinmarketcap.com/v1/"
        const val LISTING = "cryptocurrency/listings/latest"
        const val QUOTES = "cryptocurrency/quotes/latest"
        const val IMAGE_URL = "https://s2.coinmarketcap.com/static/img/coins/64x64/%s.png" //id

        const val API_KEY = "X-CMC_PRO_API_KEY"
        const val CONVERT = "convert"
        const val SORT = "sort"
        const val SORT_DIRECTION = "sort_dir"
        const val AUXILIARIES = "aux"
        const val ID = "id"
    }

    object CryptoCompare {
        const val AUTHORIZATION = "authorization"

        const val API_KEY_ROMAN_BJIT = "99cb2ed664b75035fe73b7f93d2e1e949c57f17f23f092260debf93ce1315c2d" //roman.bjit@gmail.com

        const val BASE_URL = "https://min-api.cryptocompare.com/data/"
        const val TRADES = "top/pairs"
        const val EXCHANGES = "top/exchanges/full"

        const val EXTRA_PARAMS = "extraParams"
        const val FROM_SYMBOL = "fsym"
        const val TO_SYMBOL = "tsym"

    }

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

    object Exchange {
        const val DATA = "Data"
        const val EXCHANGES = "Exchanges"
        const val MARKET = "MARKET"
        const val FROM_SYMBOL = "FROMSYMBOL"
        const val TO_SYMBOL = "TOSYMBOL"
        const val PRICE = "PRICE"
        const val VOLUME_24H = "VOLUME24HOUR"
        const val CHANGE_24H = "CHANGE24HOUR"
        const val CHANGE_PCT_24H = "CHANGEPCT24HOUR"
    }
}