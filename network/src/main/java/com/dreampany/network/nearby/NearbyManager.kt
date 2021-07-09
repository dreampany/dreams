package com.dreampany.network.nearby

import android.content.Context
import com.dreampany.network.nearby.core.NearbyApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class NearbyManager
@Inject constructor(
    context: Context
) : NearbyApi(context) {
}