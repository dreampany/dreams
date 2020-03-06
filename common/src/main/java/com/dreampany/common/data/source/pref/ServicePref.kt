package com.dreampany.common.data.source.pref

import android.content.Context
import com.dreampany.common.data.enums.ServiceState
import com.dreampany.common.misc.constant.Constants
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
        return Constants.Pref.SERVICE
    }

    fun setState(ref: String, state: ServiceState) {
        setPublicly(ref + Constants.Pref.Service.STATE, state)
    }

    fun getState(ref: String, state: ServiceState): ServiceState {
        return getPublicly(ref + Constants.Pref.Service.STATE, ServiceState::class.java, state)
    }
}