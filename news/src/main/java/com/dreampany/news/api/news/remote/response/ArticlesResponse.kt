package com.dreampany.news.api.news.remote.response

import com.dreampany.news.api.news.model.NewsArticle

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticlesResponse(
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