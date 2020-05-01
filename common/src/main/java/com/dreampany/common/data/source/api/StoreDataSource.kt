package com.dreampany.common.data.source.api

import com.dreampany.common.data.model.Store

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
    suspend fun putItem(item: Store): Long

    @Throws
    suspend fun getItem(id: String, type: String, subtype: String, state: String): Store?

    @Throws
    suspend fun getItems(type: String, subtype: String, state: String): List<String>?
}