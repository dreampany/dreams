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

    fun setJoinPressed(status: Boolean): Boolean {
        setPrivately(AppConstants.Keys.PrefKeys.JOIN_PRESSED, status)
        return status
    }

    fun isJoinPressed(): Boolean {
        return getPrivately(
            AppConstants.Keys.PrefKeys.JOIN_PRESSED,
            false
        )
    }

    fun setLoggedIn(status: Boolean): Boolean {
        setPrivately(AppConstants.Keys.PrefKeys.LOGGED_IN, status)
        return status
    }

    fun isLoggedIn(): Boolean {
        return getPrivately(
            AppConstants.Keys.PrefKeys.LOGGED_IN,
            false
        )
    }

    fun setLoggedOut(status: Boolean): Boolean {
        setPrivately(AppConstants.Keys.PrefKeys.LOGGED_OUT, status)
        return status
    }

    fun isLoggedOut(): Boolean {
        return getPrivately(
            AppConstants.Keys.PrefKeys.LOGGED_OUT,
            false
        )
    }
}