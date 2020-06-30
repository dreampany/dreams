package com.dreampany.tube.api.model

import java.util.*

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Snippet(
    val title: String,
    val description: String,
    val channelId: String,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishedAt: Date
)