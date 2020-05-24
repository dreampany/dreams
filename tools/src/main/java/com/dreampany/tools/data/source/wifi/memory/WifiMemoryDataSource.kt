package com.dreampany.tools.data.source.wifi.memory

import android.content.Context
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.api.WifiDataSource

/**
 * Created by roman on 24/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiMemoryDataSource
constructor(
    private val context: Context
) : WifiDataSource {


    @Throws
    override suspend fun gets(offset: Long, limit: Long, callback: () -> Unit): List<Wifi>? {
        TODO("Not yet implemented")
    }
}