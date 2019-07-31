package com.dreampany.history.data.source.remote

import com.dreampany.frame.api.parser.Parser
import com.dreampany.frame.misc.Constants
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
            val doc = Jsoup.connect(ref).get()
            return doc.select(Constants.Parser.PATTERN_IMAGE)
        } catch (error : Throwable) {
            Timber.e(error)
        }

        return null
    }
}