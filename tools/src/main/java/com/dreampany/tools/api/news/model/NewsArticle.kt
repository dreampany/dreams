package com.dreampany.tools.api.news.model

import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 8/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class NewsArticle(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val content: String?,
    val url: String,
    @SerializedName("urlToImage")
    val imageUrl: String?,
    val publishedAt: String
) {
    data class Source(val id: String?, val name: String?)
}