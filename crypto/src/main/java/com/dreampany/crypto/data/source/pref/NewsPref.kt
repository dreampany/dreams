package com.dreampany.crypto.data.source.pref

import android.content.Context
import com.dreampany.crypto.misc.constants.Constants
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NewsPref
@Inject constructor(
    context: Context
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.NEWS

    @Synchronized
    fun getExpireTime(query: String, language: String, offset: Long): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(query)
            append(language)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(query: String, language: String, offset: Long) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(query)
            append(language)
            append(offset)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }
}