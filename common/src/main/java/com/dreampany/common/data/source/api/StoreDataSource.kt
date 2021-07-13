package com.dreampany.common.data.source.api

import com.dreampany.common.data.model.Store

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface StoreDataSource {
    @Throws
    suspend fun isExists(id: String, type: String, subtype: String, state: String): Boolean

    @Throws
    suspend fun write(input: Store): Long

    @Throws
    suspend fun read(id: String, type: String, subtype: String, state: String): Store?

    @Throws
    suspend fun reads(type: String, subtype: String, state: String): List<Store>?

    @Throws
    suspend fun delete(store: Store): Int
}