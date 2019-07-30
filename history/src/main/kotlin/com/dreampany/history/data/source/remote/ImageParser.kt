package com.dreampany.history.data.source.remote

import com.dreampany.frame.api.parser.Parser
import com.dreampany.frame.data.model.ImageLink
import com.dreampany.frame.data.model.Link
import com.dreampany.frame.misc.Constants
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-30
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ImageParser @Inject constructor() : Parser<ImageLink>{

    override fun parse(url: String): List<ImageLink>? {
        val doc = Jsoup.connect(url).get()
        val images = doc.select(Constants.Parser.PATTERN_IMAGE)
        var result: MutableList<ImageLink>? = null
        if (!images.isNullOrEmpty()) {
            result = mutableListOf()
            for (image in images) {
                result.add(ImageLink(url, image.attr(Constants.Parser.SOURCE), image.attr(Constants.Parser.ALTERNATE)))
            }
        }
        return result
    }
}