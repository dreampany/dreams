package com.dreampany.pair.data.source.pref

import android.content.Context
import com.dreampany.common.data.enums.ServiceState
import com.dreampany.common.data.source.pref.BasePref
import com.dreampany.common.misc.constant.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Keys.PrefKeys.SERVICE
    }

    fun setState(ref: String, state: ServiceState) {
        setPublicly(ref + Constants.Keys.PrefKeys.SERVICE_STATE, state)
    }

    fun getState(ref: String, state: ServiceState): ServiceState {
        return getPublicly(ref + Constants.Keys.PrefKeys.SERVICE_STATE, ServiceState::class.java, state)
    }
}