package com.dreampany.tools.data.source.wifi.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.tools.misc.constant.WifiConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WifiPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return WifiConstants.Keys.PrefKeys.WIFI
    }


}