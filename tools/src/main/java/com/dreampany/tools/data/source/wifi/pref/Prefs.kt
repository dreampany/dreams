package com.dreampany.tools.data.source.wifi.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.tools.misc.constants.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Prefs
@Inject constructor(
    context: Context
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.Wifi.PREF
}