package com.dreampany.frame.data.source.pref

import android.content.Context

/**
 * Created by roman on 2019-07-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class FramePref(context: Context) : BasePref(context) {

    private val VERSION_CODE = "version_code"

    fun setVersionCode(versionCode: Int) {
        setPublicly(VERSION_CODE, versionCode)
    }

    fun getVersionCode(): Int {
        return getPrivately(VERSION_CODE, 0)
    }
}