package com.dreampany.news.api.news.misc

import java.util.*

/**
 * Created by roman on 13/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {
    object Values {
        val CATEGORIES = Arrays.asList(
            "business",
            "entertainment",
            "general",
            "health",
            "science",
            "sports",
            "technology"
        )
    }

    object Api {
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
}