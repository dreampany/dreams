package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
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
class BlockPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.Block.CONTACT
    }
}