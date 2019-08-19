package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.FramePref
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
        setPrivately(Constants.Language.LANGUAGE, language)
    }

    fun getLanguage(language: Language): Language {
        return getPrivately(Constants.Language.LANGUAGE, Language::class.java, language)
    }
}