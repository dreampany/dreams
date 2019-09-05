package com.dreampany.translation.misc

import com.dreampany.framework.misc.Constants

/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Network {
        const val HEADER_CONNECTION_CLOSE = "Connection:close"
    }

    object Yandex {
        const val URL = "http://translate.yandex.com/"
        const val TRANSLATE_BASE_URL = "https://translate.yandex.net"
        const val TRANSLATE_API_KEY_ROMAN_BJIT_QURAN =
            "trnsl.1.1.20190510T101322Z.98458679b6c802ad.8fa66e27efd6ea413c6d7c6655aa5726cd295608"
        const val TRANSLATE_API_KEY_ROMAN_BJIT_WORD =
            "trnsl.1.1.20190128T082459Z.eb601fb8f09485f3.115b641f8d93b92d40615a63956e4e8e03473a2f"

        const val TRANSLATION_END_POINT = "/api/v1.5/tr.json/translate"
        const val KEY = "key"
        const val TEXT = "text"
        const val LANGUAGE = "lang"
    }

    object Translation {
        const val ID = Constants.Key.ID
        const val INPUT = "input"
        const val SOURCE = "source"
        const val TARGET = "target"
    }

    object FirebaseKey {
        const val TRANSLATIONS = "translations"
    }

    object Sep {
        const val SPACE = Constants.Sep.SPACE
        const val HYPHEN = Constants.Sep.HYPHEN
    }

    object Default {
        val NULL = Constants.Default.NULL
        const val INT = Constants.Default.INT
        const val LONG = Constants.Default.LONG
        const val STRING = Constants.Default.STRING
    }
}