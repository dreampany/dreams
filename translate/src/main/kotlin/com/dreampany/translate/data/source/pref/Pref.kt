package com.dreampany.translate.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.BasePrefKt
import com.dreampany.language.Language
import com.dreampany.translate.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class Pref @Inject constructor(context: Context) : BasePrefKt(context) {

    fun setSourceLanguage(language: Language) {
        setPrivately(Constants.Language.SOURCE_LANGUAGE, language)
    }

    fun getSourceLanguage(language: Language): Language {
        return getPrivately(Constants.Language.SOURCE_LANGUAGE, Language::class.java, language)
    }

    fun setTargetLanguage(language: Language) {
        setPrivately(Constants.Language.TARGET_LANGUAGE, language)
    }

    fun getTargetLanguage(language: Language): Language {
        return getPrivately(Constants.Language.TARGET_LANGUAGE, Language::class.java, language)
    }

}