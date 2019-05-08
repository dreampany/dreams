package com.dreampany.quran.misc

import android.content.Context
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.TextUtil
import com.dreampany.quran.R
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    companion object Screen {
        fun lastAppId(context: Context): String = AndroidUtil.getLastApplicationId(context)

        fun app(context: Context): String = lastAppId(context) + Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)
        fun navigation(context: Context): String = lastAppId(context) + Sep.HYPHEN + "navigation"
        fun tools(context: Context): String = lastAppId(context) + Sep.HYPHEN + "tools"
        fun surahs(context: Context): String = lastAppId(context) + Sep.HYPHEN + "surahs"
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

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
    }

    object AyahKey {
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
}