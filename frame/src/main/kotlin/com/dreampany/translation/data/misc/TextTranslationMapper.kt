package com.dreampany.translation.data.misc

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.model.TextTranslationResponse
import com.dreampany.translation.misc.Constants
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
class TextTranslationMapper
@Inject constructor(
    @TextTranslateAnnote val map: SmartMap<String, TextTranslation>,
    @TextTranslateAnnote val cache: SmartCache<String, TextTranslation>
) {

    fun isExists(translation: TextTranslation): Boolean {
        return map.contains(toId(translation))
    }

    fun toId(source: String, target: String, input: String): String {
        return input.plus(source).plus(target)
    }

    fun toId(translation: TextTranslation): String {
        return toId(translation.input, translation.source, translation.target)
    }

    fun toLanguagePair(source: String, target: String): String {
        return source.plus(Constants.Sep.HYPHEN).plus(target)
    }

    fun toItem(
        input: String,
        source: String,
        target: String,
        result: TextTranslationResponse
    ): TextTranslation? {
        val id = toId(input, source, target)
        val time = TimeUtil.currentTime()
        val output = DataUtil.joinString(result.text, Constants.Sep.SPACE)
        output?.let {
            return TextTranslation(id, time, input, source, target, it)
        }
        return null
    }
}