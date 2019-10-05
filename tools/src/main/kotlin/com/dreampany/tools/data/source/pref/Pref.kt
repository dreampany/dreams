package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.language.Language
import com.dreampany.tools.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref
@Inject constructor(
    context: Context
) : FramePref(context) {

    init {
    }

    fun hasNotification(): Boolean {
        return true
    }

    fun setLanguage(language: Language) {
        setPublicly(Constants.Pref.LANGUAGE, language)
    }

    fun getLanguage(language: Language): Language {
        return getPublicly(Constants.Pref.LANGUAGE, Language::class.java, language)
    }

    fun setLevel(level: Level) {
        setPublicly(Constants.Pref.LEVEL, level)
    }

    fun getLevel(level: Level): Level {
        return getPublicly(Constants.Pref.LEVEL, Level::class.java, level)
    }

    fun hasDefaultPoint(): Boolean {
        return getPublicly(Constants.Pref.DEFAULT_POINT, false)
    }
}