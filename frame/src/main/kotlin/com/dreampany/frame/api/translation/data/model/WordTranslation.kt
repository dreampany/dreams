package com.dreampany.frame.api.translation.data.model

/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class WordTranslation(
    val word: String,
    val sourceLanguage: String,
    val targetLanguage: String
) {
    var text: MutableList<String>? = null
}