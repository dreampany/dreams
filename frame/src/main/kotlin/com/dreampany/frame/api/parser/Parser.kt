package com.dreampany.frame.api.parser

import com.dreampany.frame.data.model.Link
import com.dreampany.frame.misc.Constants
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Parser @Inject constructor() {

    fun parseImageLink(url: String): List<Link>? {
        val doc = Jsoup.connect(url).get()
        val images = doc.select(Constants.Parser.PATTERN_IMAGE)
        var result: MutableList<Link>? = null
        if (!images.isNullOrEmpty()) {
            result = mutableListOf()
            for (image in images) {
                result.add(Link(image.attr(Constants.Parser.SOURCE), image.attr(Constants.Parser.ALTERNATE)))
            }
        }
        return result
    }
}