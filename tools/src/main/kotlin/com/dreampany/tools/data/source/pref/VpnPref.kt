package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class VpnPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivatePrefName(context: Context): String? {
        return Constants.Pref.SERVER
    }

    fun commitServerTime() {
        setPrivately(Constants.Pref.SERVER_TIME, TimeUtilKt.currentMillis())
    }

    fun getServerTime() : Long {
        return getPrivately(Constants.Pref.SERVER_TIME, 0L)
    }

    fun getDownload() : Long {
        return getPrivately(Constants.Pref.DOWNLOAD, 0L)
    }

    fun setDownload(download: Long) {
        setPrivately(Constants.Pref.DOWNLOAD, download)
    }

    fun getUpload() : Long {
        return getPrivately(Constants.Pref.UPLOAD, 0L)
    }

    fun setUpload(upload: Long) {
        setPrivately(Constants.Pref.UPLOAD, upload)
    }
}