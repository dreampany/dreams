package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.ui.enums.UiType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ConfigPref @Inject constructor(context: Context) : FramePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.CONFIG
    }

    fun setScreen(type: UiType, screen: String) {
        setPublicly(Constants.Pref.SCREEN.plus(type.name), screen)
    }

    fun getScreen(type: UiType): String {
        return getPublicly(Constants.Pref.SCREEN.plus(type.name), Constants.Default.STRING)
    }
}