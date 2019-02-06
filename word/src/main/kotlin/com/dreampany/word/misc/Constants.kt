package com.dreampany.word.misc

import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
object Constants {

    object Assets {
        const val WORDS_COMMON = "words_common.txt"
        const val WORDS_ALPHA = "words_alpha.txt"
    }

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
        const val WORD_SUGGESTION = 2
        const val WORD_OCR = 1000
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
        val WordPeriod = TimeUnit.SECONDS.toMillis(10)
    }

    object KEY {
        const val WORDS = "words"
    }
}