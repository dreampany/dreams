package com.dreampany.pair.data.source.api

import com.dreampany.common.data.source.api.CoroutineDataSource
import kotlinx.coroutines.Deferred

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationDataSource : CoroutineDataSource<Any> {
    override suspend fun isEmpty(): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun putItem(t: Any): Deferred<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(id: String): Deferred<Any> {
        TODO("Not yet implemented")
    }
}