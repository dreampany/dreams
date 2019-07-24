package com.dreampany.history.data.source.remote

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class WikiHistory(
    val year: Int,
    val text: String,
    val html: String,
    val links: MutableList<WikiLink>
) {
}