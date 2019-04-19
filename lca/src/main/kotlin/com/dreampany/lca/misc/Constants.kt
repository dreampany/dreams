package com.dreampany.lca.misc

import android.content.Context
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.TextUtil
import com.dreampany.lca.R
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    companion object Screen {
        fun lastAppId(context: Context): String = AndroidUtil.getLastApplicationId(context)

        fun app(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)
        fun navigation(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "navigation"
        fun tools(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "tools"
        fun coins(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "coins"
        fun coin(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "coin"
        fun coinDetails(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "coin_details"
        fun coinMarket(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "coin_market"
        fun coinGraph(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "coin_graph"
        fun favoriteCoins(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "favorite_coins"
        fun coinAlerts(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "coin_alerts"
        fun coinAlert(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "coin_alert"
        fun ico(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "ico"
        fun icoLive(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "ico_live"
        fun icoUpcoming(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "ico_upcoming"
        fun icoFinished(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "ico_finished"
        fun news(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + "news"
    }

    object Tag {
        const val CURRENCY_PICKER = "currency_picker"
    }

    object Notify {
        const val ALERT_ID = 201
        const val NEWS_ID = 202
        const val ALERT_CHANNEL_ID = "alert_channel_id"
        const val NEWS_CHANNEL_ID = "news_channel_id"
    }

    object Worker {
        const val NOTIFY = "notify_worker"
    }

    object Sep {
        const val SPACE = " "
        const val HYPHEN = "-"
        const val HYPHEN_SPACE = "- "
        const val SPACE_HYPHEN_SPACE = " - "
        const val COMMA = ","
        const val COMMA_SPACE = ", "
        const val UP = ">"
        const val DOWN = "<"
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
        const val CmcApiUrlV1 = "https://pro-api.coinmarketcap.com/v1/"
        const val CoinMarketCapGraphApiUrl = "https://graphs2.coinmarketcap.com/"

        const val CryptoCompareApiUrl = "https://min-api.cryptocompare.com/data/"
        const val CryptoCompareMarketOverviewUrl = "https://www.cryptocompare.com/exchanges/binance/overview/%s"

        const val IcoWatchListApiUrl = "https://api.icowatchlist.com/public/v1/"
    }

    object ImageUrl {
        const val CoinMarketCapImageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/%d.png" //id reference
    }

    object FirestoreKey {
        const val CRYPTO = "crypto"
        const val COINS = "coins"
    }

    object CmcCoinKey {
        const val ID = "id"
        const val NAME = "name"
        const val SYMBOL = "symbol"
        const val SLUG = "slug"
        const val RANK = "cmc_rank"
        const val MARKET_PAIRS = "num_market_pairs"
        const val CIRCULATING_SUPPLY = "circulating_supply"
        const val TOTAL_SUPPLY = "total_supply"
        const val MAX_SUPPLY = "max_supply"
        const val LAST_UPDATED = "last_updated"
        const val DATE_ADDED = "date_added"
        const val TAGS = "tags"
        const val QUOTE = "quote"
    }

    object CoinKey {
        const val ID = "id"
        const val COIN_ID = "coin_id"
        const val NAME = "name"
        const val SYMBOL = "symbol"
        const val SLUG = "slug"
        const val RANK = "rank"
        const val MARKET_PAIRS = "market_pairs"
        const val CIRCULATING_SUPPLY = "circulating_supply"
        const val TOTAL_SUPPLY = "total_supply"
        const val MAX_SUPPLY = "max_supply"
        const val LAST_UPDATED = "last_updated"
        const val DATE_ADDED = "date_added"
        const val TAGS = "tags"
    }

    object Limit {
        const val COIN_START_INDEX = 0
        const val COIN_PAGE = 100
        const val COIN_MARKET = 50
        const val COIN_EXCHANGE = 50
        const val ICO = 50
        const val NEWS = 50
        const val CMC_KEY = 5
    }

    object Time {
        val Listing = TimeUnit.MINUTES.toMillis(2) //get listing per 7 days
        val Coin = TimeUnit.MINUTES.toMillis(1) // Every ~1 minute; as per coinmarketcap limit 30 per minute
        val Graph = TimeUnit.MINUTES.toMillis(5) //as per coinmarketcap limit 30 per minute
        val IcoPeriod = TimeUnit.MINUTES.toMillis(10)
    }

    object Period {
        val Coin = TimeUnit.SECONDS.toMillis(30)
        val Notify = TimeUnit.MINUTES.toSeconds(5)
    }

    object Delay {
        val CoinListing = TimeUnit.DAYS.toMillis(1)
        val Ico = TimeUnit.HOURS.toMillis(1)
        val News = TimeUnit.HOURS.toMillis(1)
        val Notify = TimeUnit.MINUTES.toSeconds(1)
        val CmcKey = TimeUnit.MINUTES.toSeconds(1)
    }
}