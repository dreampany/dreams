package com.dreampany.framework.data.source.api

import kotlinx.coroutines.Deferred

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoroutineDataSource<T> {
    suspend fun isEmpty(): Deferred<Boolean>
    suspend fun putItem(t: T): Deferred<Long>
    suspend fun getItem(id: String): Deferred<T>
}