package com.dreampany.history.data.source.remote

import com.dreampany.frame.api.parser.Parser
import com.dreampany.history.misc.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-30
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ImageParser @Inject constructor() : Parser<Element> {

    override fun parse(ref: String): List<Element>? {
        try {
            val connection = Jsoup.connect(ref).timeout(Constants.Time.JSOUP.toInt())
            val doc = connection.get()
            val elements = doc.getElementsByTag(Constants.ImageParser.PATTERN_IMAGE_TAG)
            return elements
        } catch (error : Throwable) {
            Timber.e(error)
        }

        return null
    }
}