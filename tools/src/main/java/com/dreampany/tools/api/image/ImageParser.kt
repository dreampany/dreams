package com.dreampany.tools.api.image

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ImageParser
@Inject constructor() {
    fun parse(ref: String): List<Element>? {
        try {
            val connection = Jsoup.connect(ref).timeout(Constants.Time.JSOUP.toInt())
            val doc = connection.get()
            val elements = doc.getElementsByTag(Constants.Keys.PATTERN_IMAGE_TAG)
            return elements
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return null
    }
}