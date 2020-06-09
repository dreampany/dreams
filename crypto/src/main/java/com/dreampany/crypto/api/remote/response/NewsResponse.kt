package com.dreampany.crypto.api.remote.response

import com.dreampany.crypto.api.model.NewsArticle

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NewsResponse(
    val status: String,
    val code: String? = null,
    val message: String? = null,
    val articles: List<NewsArticle>
) {
    val hasError: Boolean
        get() = code != null

    val isEmpty: Boolean
        get() = articles.isNullOrEmpty()
}