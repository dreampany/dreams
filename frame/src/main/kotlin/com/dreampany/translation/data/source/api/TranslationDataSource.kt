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
    fun isReady(target: String): Boolean
    fun ready(target: String)
    fun isExists(source: String, target: String, input: String): Boolean
    fun isExistsRx(source: String, target: String, input: String): Maybe<Boolean>
    fun getItem(source: String, target: String, input: String): TextTranslation?
    fun getItemRx(source: String, target: String, input: String): Maybe<TextTranslation>
}