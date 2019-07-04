package com.dreampany.translation.data.source.remote

import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.misc.TextTranslateMapper
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class TextTranslateRemoteDataSource
constructor(
    val network: NetworkManager,
    val mapper:TextTranslateMapper
) {
}