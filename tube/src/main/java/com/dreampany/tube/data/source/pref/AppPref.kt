package com.dreampany.tube.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.tube.misc.AppConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String = AppConstants.Keys.Pref.PREF

}