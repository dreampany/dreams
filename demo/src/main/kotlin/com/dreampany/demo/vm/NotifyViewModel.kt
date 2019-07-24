package com.dreampany.demo.vm

import android.app.Application
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.network.manager.NetworkManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NotifyViewModel @Inject constructor(
    val application: Application,
    val rx: RxMapper,
    val ex: AppExecutors,
    val rm: ResponseMapper,
    val network: NetworkManager
) {
}