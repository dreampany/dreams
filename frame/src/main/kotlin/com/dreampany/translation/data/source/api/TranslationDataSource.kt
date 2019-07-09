package com.dreampany.translation.data.source.api

import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.translation.data.model.TextTranslation
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface TranslationDataSource : DataSource<TextTranslation> {
    fun isExists(input: String, source: String, target: String): Boolean
    fun isExistsRx(input: String, source: String, target: String): Maybe<Boolean>
    fun getItem(input: String, source: String, target: String): TextTranslation?
    fun getItemRx(input: String, source: String, target: String): Maybe<TextTranslation>
}