package com.dreampany.tools.misc.constants

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 14/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NewsConstants {
    object Keys {
        object Pref {
            const val NEWS = "news"
            const val EXPIRE = "expire"
        }

        object Room {
            const val TYPE_NEWS = "news"
        }

        object News {
            const val SOURCE_ID = "source_id"
        }
    }

    object Common {
        const val START = "start"
        const val LIMIT = "limit"
        const val STATUS = "status"
        const val DATA = "data"
    }

    object NewsApi {
        const val API_KEY_ROMAN_BJIT = "27e17471f26047a893bc0824c323799d"

        const val BASE_URL = "https://newsapi.org/v2/"

        const val API_KEY = "X-Api-Key"
        const val HEADLINES = "top-headlines"
        const val EVERYTHING = "everything"

        const val QUERY_IN_TITLE = "qInTitle"
        const val COUNTRY = "country"
        const val LANGUAGE = "language"
        const val CATEGORY = "category"
        const val OFFSET = "page"
        const val LIMIT = "pageSize"
    }

    object Times {
        val NEWS = TimeUnit.HOURS.toMillis(1)
    }
}