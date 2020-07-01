package com.dreampany.tube.api.model

import java.util.*

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SearchSnippet(
    val title: String,
    val description: String,
    val channelId: String,
    val channelTitle: String,
    val thumbnails : Map<String, Thumbnail>,
    val liveBroadcastContent: String,
    val publishedAt: Date
)