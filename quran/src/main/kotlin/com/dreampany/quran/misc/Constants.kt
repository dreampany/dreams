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

        fun app(context: Context): String =
            lastAppId(context) + Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)

        fun navigation(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.NAVIGATION
        fun tools(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.TOOLS
        fun surahs(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.SURAHS
        fun ayahs(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.AYAHS
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

    object Tag {
        const val NAVIGATION = "navigation"
        const val TOOLS = "tools"
        const val SURAHS = "surahs"
        const val AYAHS = "ayahs"
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
    }

    object Ayah {
        const val NUMBER = "number"
        const val NUMBER_OF_SURAH = "number_of_surah"
        const val NUMBER_IN_SURAH = "number_in_surah"
        const val LOCAL_AUDIO_URL = "local_audio_url"
        const val REMOTE_AUDIO_URL = "remote_audio_url"
        const val HIZB_QUARTER = "hizb_quarter"
    }

    object Surah {
        const val NUMBER = "number"
    }

    object Assets {
        const val QURAN_TANZIL = "tanzil/quran-uthmani.xml"
    }
}