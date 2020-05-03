package com.dreampany.framework.data.source.api

import com.dreampany.framework.data.model.Store

/**
 * Created by roman on 20/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StoreDataSource {
    @Throws
    suspend fun isExists(id: String, type: String, subtype: String, state: String): Boolean

    @Throws
    suspend fun insert(item: Store): Long

    @Throws
    suspend fun getStore(id: String, type: String, subtype: String, state: String): Store?

    @Throws
    suspend fun getStores(type: String, subtype: String, state: String): List<Store>?

    @Throws
    suspend fun delete(store: Store): Int
}