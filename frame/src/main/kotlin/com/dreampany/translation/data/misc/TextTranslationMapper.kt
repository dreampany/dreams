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
        return toId(translation.source, translation.target, translation.input)
    }

    fun toLanguagePair(source: String, target: String): String {
        return source.plus(Constants.Sep.HYPHEN).plus(target)
    }

    fun toItem(
        source: String,
        target: String,
        input: String,
        output: String
    ): TextTranslation? {
        val id = toId(source, target, input)
        val time = TimeUtil.currentTime()
        return TextTranslation(id, source, target, input, output)
    }

    fun toItem(
        source: String,
        target: String,
        input: String,
        output: TextTranslationResponse
    ): TextTranslation? {
        val id = toId(source, target, input)
        val time = TimeUtil.currentTime()
        val outputText = DataUtil.joinString(output.text, Constants.Sep.SPACE)
        outputText?.let {
            return TextTranslation(id, source, target, input, it)
        }
        return null
    }
}