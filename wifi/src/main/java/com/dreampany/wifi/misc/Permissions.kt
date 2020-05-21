package com.dreampany.wifi.misc

import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

/**
 * Created by roman on 22/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Permissions(private val context: Context) {

    val isLocationEnabled: Boolean
        get() {
            try {
                val location = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                return LocationManagerCompat.isLocationEnabled(location)
            } catch (error: Throwable) {
                return false
            }
        }
}