package com.dreampany.crypto.api.crypto.misc

import com.dreampany.framework.misc.constant.Constant

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

            const val CURRENCIES = "fiat/map"
            const val META = "cryptocurrency/info"
            const val COINS = "cryptocurrency/listings/latest"
            const val QUOTES = "cryptocurrency/quotes/latest"
            const val GRAPH =
                "currencies/{slug}/{${Keys.CMC.START_TIME}}/{${Keys.CMC.END_TIME}}"

        }

        object CryptoCompare {
            const val BASE_URL = "https://min-api.cryptocompare.com/data/"
            const val AUTHORIZATION = "authorization"

            const val TRADES = "top/pairs"
            const val EXCHANGES = "top/exchanges/full"

            const val EXTRA_PARAMS = "extraParams"
            const val FROM_SYMBOL = "fsym"
            const val TO_SYMBOL = "tsym"

        }

        object Gecko {
            const val BASE_URL = "https://api.coingecko.com/api/v3/"

            const val TICKERS = "coins/{id}/tickers"
            const val ID = "id"
            const val INCLUDE_IMAGE = "include_exchange_logo"
        }
    }

    object Keys {
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
            const val TOTAL_COUNT = "total_count"
        }

        object CMC {

            const val MARKET_PAIRS = "num_market_pairs"
            const val RANK = "cmc_rank"
            const val DATE_ADDED = "date_added"
            const val TAGS = "tags"
            const val PLATFORM = "platform"
            const val MAX_SUPPLY = "max_supply"
            const val CIRCULATING_SUPPLY = "circulating_supply"
            const val TOTAL_SUPPLY = "total_supply"
            const val MARKET_CAP = "market_cap_by_total_supply"

            const val VOLUME_24H = "volume_24h"
            const val VOLUME_24H_REPORTED = "volume_24h_reported"
            const val VOLUME_7D = "volume_7d"
            const val VOLUME_7D_REPORTED = "volume_7d_reported"
            const val VOLUME_30D = "volume_30d"
            const val VOLUME_30D_REPORTED = "volume_30d_reported"

            const val METALS = "include_metals"
            const val SLUG = "slug"
            const val START_TIME = "start_time"
            const val END_TIME = "end_time"
            const val PRICE_BTC = "price_btc"
            const val PRICE_USD = "price_usd"
            const val VOLUME_USD = "volume_usd"
            const val TOKEN_ADDRESS = "token_address"
            const val CHANGE_1H = "percent_change_1h"
            const val CHANGE_24H = "percent_change_24h"
            const val CHANGE_7D = "percent_change_7d"
            const val LAST_UPDATED = "last_updated"

            const val CONVERT_ID = "convert_id"
            const val SORT = "sort"
            const val SORT_DIRECTION = "sort_dir"
            const val AUX = "aux"

            const val ICON = "logo"

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

        object Trade {
            const val DATA = "Data"
            const val EXCHANGE = "exchange"
            const val FROM_SYMBOL = "fromSymbol"
            const val TO_SYMBOL = "toSymbol"
            const val VOLUME_24H = "volume24h"
            const val VOLUME_24H_TO = "volume24hTo"
        }

    }

    object Values {
        object CMC {
            val COIN_AUX = StringBuilder(Keys.CMC.MARKET_PAIRS).append(Constant.Sep.COMMA)
                .append(Keys.CMC.RANK).append(Constant.Sep.COMMA)
                .append(Keys.CMC.DATE_ADDED).append(Constant.Sep.COMMA)
                .append(Keys.CMC.TAGS).append(Constant.Sep.COMMA)
                .append(Keys.CMC.PLATFORM).append(Constant.Sep.COMMA)
                .append(Keys.CMC.MAX_SUPPLY).append(Constant.Sep.COMMA)
                .append(Keys.CMC.CIRCULATING_SUPPLY).append(Constant.Sep.COMMA)
                .append(Keys.CMC.TOTAL_SUPPLY).append(Constant.Sep.COMMA)
                .append(Keys.CMC.MARKET_CAP).append(Constant.Sep.COMMA)
                .append(Keys.CMC.VOLUME_24H_REPORTED).append(Constant.Sep.COMMA)
                .append(Keys.CMC.VOLUME_7D).append(Constant.Sep.COMMA)
                .append(Keys.CMC.VOLUME_7D_REPORTED).append(Constant.Sep.COMMA)
                .append(Keys.CMC.VOLUME_30D).append(Constant.Sep.COMMA)
                .append(Keys.CMC.VOLUME_30D_REPORTED).toString()
        }
    }
}