package com.dreampany.nearby.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.nearby.misc.AppConstants
import com.dreampany.network.nearby.core.NearbyApi
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

    @Synchronized
    fun getNearbyType(): NearbyApi.Type {
        return getPrivately(
            AppConstants.Keys.Pref.NEARBY_TYPE,
            NearbyApi.Type::class.java,
            NearbyApi.Type.PTP
        )
    }

    @Synchronized
    fun setNearbyType(type: NearbyApi.Type) {
        setPrivately(AppConstants.Keys.Pref.NEARBY_TYPE, type)
    }
}