package com.dreampany.pair.data.source.repo

import android.content.Context
import com.dreampany.common.data.source.pref.BasePref
import com.dreampany.pair.misc.constant.AppConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PrefRepo
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return AppConstants.Keys.PrefKeys.PREF
    }

    fun setLoggedOut(loggedOut: Boolean): Boolean {
        setPrivately(AppConstants.Keys.PrefKeys.LOGGED_OUT, loggedOut)
        return loggedOut
    }

    fun isLoggedOut(): Boolean {
        return getPrivately(
            AppConstants.Keys.PrefKeys.LOGGED_OUT,
            false
        )
    }
}