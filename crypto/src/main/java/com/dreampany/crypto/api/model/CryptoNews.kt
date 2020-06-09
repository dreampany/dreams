package com.dreampany.crypto.api.model

import com.dreampany.crypto.api.misc.ApiConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 8/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoNews(
    val id: Long,
    val guid: String,
    val title: String,
    val body: String,
    val url: String,
    val source: String,
    @SerializedName(value = ApiConstants.News.IMAGE_URL)
    val imageUrl: String,
    @SerializedName(value = ApiConstants.News.UP_VOTES)
    val upVotes: Long,
    @SerializedName(value = ApiConstants.News.DOWN_VOTES)
    val downVotes: Long,
    @SerializedName(value = ApiConstants.News.LANGUAGE)
    val language: String,
    val tags: String,
    val categories: String,
    @SerializedName(value = ApiConstants.News.SOURCE_INFO)
    val sourceInfo: SourceInfo,
    @SerializedName(value = ApiConstants.News.PUBLISHED_ON)
    val publishedOn: Long
    ) {

    data class SourceInfo(
        @SerializedName(value = ApiConstants.News.NAME)
        val name: String,
        @SerializedName(value = ApiConstants.News.LANGUAGE)
        val language: String,
        @SerializedName(value = ApiConstants.News.IMAGE_URL)
        val image: String
    )
}