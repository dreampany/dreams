package com.dreampany.tools.api.image

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Time {
        val JSOUP = TimeUnit.SECONDS.toMillis(20)
    }

    object Keys {
        const val PATTERN_IMAGE_TAG = "img"
        const val BASE_URL = "baseUrl"
        const val HREF = "href"
        const val SOURCE = "src"
        const val ALTERNATE = "alt"
        const val WIDTH = "width"
        const val HEIGHT = "height"
    }
}