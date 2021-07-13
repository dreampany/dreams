package com.dreampany.common.data.source.mapper

import com.dreampany.common.data.model.Store
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class StoreMapper @Inject constructor() {

    private val stores: MutableMap<String, Store>

    init {
        stores = Maps.newConcurrentMap()
    }

    fun isExists(id: String): Boolean {
        return stores.contains(id)
    }

    fun isExists(id: String, type: String, subtype: String, state: String): Boolean {
        if (isExists(id)) {
            val item = read(id)
            return item?.hasProperty(type, subtype, state) ?: false
        }
        return false
    }

    fun write(input: Store) {
        stores.put(input.id, input)
    }

    fun read(id: String): Store? {
        return stores.get(id)
    }

    fun read(id: String, type: String, subtype: String, state: String, extra: String? = null): Store? {
        return if (isExists(id, type, subtype, state)) read(id)
        else Store(
            id = id,
            type = type,
            subtype = subtype,
            state = state,
            extra = extra
        )
    }
}