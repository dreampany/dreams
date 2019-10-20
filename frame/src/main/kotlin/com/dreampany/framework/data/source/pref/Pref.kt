package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref @Inject constructor(context: Context) : FramePref(context) {

    override fun getPrivateName(context: Context): String? {
        return Constants.Pref.PREF
    }
}