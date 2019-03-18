package com.dreampany.lca.misc

import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
object Constants {

    object Sep {
        const val SEP_COMMA = ","
    }

    object Key {
        const val CMC_PRO_ROMAN_BJIT = "2526f063-e73d-4fb9-b221-2bd8c8097525" //roman.bjit@gmail.com
        const val CMC_PRO_IFTE_NET = "e5c34607-689c-4530-886e-0d62c923797a" //ifte.net@gmail.com
        const val CMC_PRO_DREAMPANY = "d158ff45-ef74-4562-8984-8d717f422df8" //dreampanymail@gmail.com
        const val CMC_SANDBOX = "ba266b8e-abf4-466f-84cd-38700d6eb8f0"
    }

    object Structure {
        const val ARRAY = "array"
    }

    object Api {
        const val CoinMarketCapSiteUrl = "https://coinmarketcap.com/currencies/%s";
        const val CoinMarketCapApiUrl = "https://api.coinmarketcap.com/v2/"
        const val CMCApiUrlV1 = "https://pro-api.coinmarketcap.com/v1/"
        const val CoinMarketCapGraphApiUrl = "https://graphs2.coinmarketcap.com/"

        const val CryptoCompareApiUrl = "https://min-api.cryptocompare.com/data/"
        const val CryptoCompareMarketOverviewUrl = "https://www.cryptocompare.com/exchanges/binance/overview/%s"

        const val IcoWatchListApiUrl = "https://api.icowatchlist.com/public/v1/"
    }

    object ImageUrl {
        const val CoinMarketCapImageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/%d.png" //id reference
    }

    object Limit {
        const val COIN_START_INDEX = 0
        const val COIN_PAGE = 20
        const val COIN_MARKET = 100
        const val COIN_EXCHANGE = 50
        const val ICO = 500
        const val NEWS = 500
    }

    object Time {
        val Listing = TimeUnit.DAYS.toMillis(7) //get listing per 7 days
        val Coin = TimeUnit.SECONDS.toMillis(90) //as per coinmarketcap limit 30 per minute
        val IcoPeriod = TimeUnit.MINUTES.toMillis(10)
        val NewsPeriod = TimeUnit.MINUTES.toMillis(10)
    }

    object Period {
        val Coin = TimeUnit.SECONDS.toMillis(30)
        val CoinDetails = TimeUnit.SECONDS.toMillis(20)
        val Notify = TimeUnit.MINUTES.toSeconds(5)
    }

    object Delay {
        val CoinListing = TimeUnit.DAYS.toMillis(1)
        val Ico = TimeUnit.HOURS.toMillis(1)
        val News = TimeUnit.HOURS.toMillis(1)
        val Notify = TimeUnit.MINUTES.toSeconds(1)
    }
}