package com.dreampany.translation.data.misc

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.misc.TextTranslateAnnote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class TextTranslateMapper
@Inject constructor(
    @TextTranslateAnnote val map: SmartMap<String, TextTranslation>,
    @TextTranslateAnnote val cache: SmartCache<String, TextTranslation>
) {

    fun isExists(translation: TextTranslation): Boolean {
        return map.contains(translation.getId())
    }
}