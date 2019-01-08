package com.dreampany.word.misc

import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
object Constants {

    object Count {
        const val WORD_COMMON = 1000
        const val WORD_ALPHA = 370099
        const val WORD_RECENT = 100
        const val WORD_RECENT_LETTER = 4
    }

    object Limit {
        const val WORD_RESOLVE = 10
        const val WORD_RECENT = 100
        const val WORD_SEARCH = 1000
        const val WORD_OCR = 1000
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
        val WordPeriod = TimeUnit.SECONDS.toMillis(5)
    }

    object KEY {
        const val WORDS = "words"
    }
}