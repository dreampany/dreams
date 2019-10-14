package com.dreampany.tools.api.player

import com.google.android.exoplayer2.upstream.DataSource
import okhttp3.OkHttpClient


/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DataSourceFactory(
    val http: OkHttpClient
): DataSource.Factory {




    override fun createDataSource(): DataSource {

    }
}