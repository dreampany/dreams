package com.dreampany.device

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by roman on 7/17/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class DeviceInfo
@Inject constructor(
    @ApplicationContext private val context: Context
) {

    val model: String
        get() = Build.MODEL

}