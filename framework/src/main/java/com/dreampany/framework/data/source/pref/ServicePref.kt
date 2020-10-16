package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.data.enums.ServiceState
import com.dreampany.framework.misc.constant.Constant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 6/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ServicePref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constant.Keys.Pref.SERVICE
    }

    fun setState(ref: String, state: ServiceState) {
        setPublicly(ref + Constant.Keys.Pref.SERVICE_STATE, state)
    }

    fun getState(ref: String, state: ServiceState): ServiceState {
        return getPublicly(ref + Constant.Keys.Pref.SERVICE_STATE, ServiceState::class.java, state) ?: state
    }
}