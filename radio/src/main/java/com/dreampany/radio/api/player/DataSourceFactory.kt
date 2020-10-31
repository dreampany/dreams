package com.dreampany.radio.api.player

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.TransferListener
import okhttp3.OkHttpClient


/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DataSourceFactory(
    val http: OkHttpClient,
    val transferListener: TransferListener,
    val listener: IcyDataSource.Listener,
    val retryTimeout: Long = 0,
    val retryDelay: Long = 0
): DataSource.Factory {

    override fun createDataSource(): DataSource {
        return IcyDataSource(
            http,
            transferListener,
            listener,
            retryTimeout,
            retryDelay
        )
    }
}