package com.dreampany.demo.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.FramePrefKt
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref @Inject constructor(context: Context) : FramePrefKt(context) {

    init {
    }

    fun hasNotification(): Boolean {
        return true
    }

}