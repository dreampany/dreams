package com.dreampany.hello.data.source.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.hello.data.model.Auth
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AuthMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val timeRepo: TimeRepo
) {

    private val auths: MutableMap<String, Auth>

    init {
        auths = Maps.newConcurrentMap()
    }

    @Synchronized
    fun has(id : String) : Boolean = auths.containsKey(id)

    @Synchronized
    fun write(input: Auth) {
        auths.put(input.id, input)
    }

    @Synchronized
    fun read(id: String): Auth? {
        return auths.get(id)
    }
}