package com.dreampany.hello.data.source.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.hello.data.model.User
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
class UserMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val timeRepo: TimeRepo
) {

    private val users: MutableMap<String, User>
    private val indexes : MutableMap<String, Long>
    private val timestamps : MutableMap<String, Long>
    private val onlines : MutableMap<String, Boolean>

    init {
        users = Maps.newConcurrentMap()
        indexes = Maps.newConcurrentMap()
        timestamps = Maps.newConcurrentMap()
        onlines = Maps.newConcurrentMap()
    }

    @Synchronized
    fun has(id : String) : Boolean = users.containsKey(id)

    @Synchronized
    fun write(input: User) {
        users.put(input.id, input)
    }

    @Synchronized
    fun read(id: String): User? {
        return users.get(id)
    }

    @Synchronized
    fun writeIndex(id : String, index : Long) {
        indexes.put(id, index)
    }

    @Synchronized
    fun writeTimestamp(id : String, timestamp : Long) {
        timestamps.put(id, timestamp)
    }

    @Synchronized
    fun writeOnline(id : String, online : Boolean) {
        onlines.put(id, online)
    }

    @Synchronized
    fun index(id : String) : Long = indexes.get(id) ?: Constant.Default.LONG

    @Synchronized
    fun timestamp(id : String) : Long = timestamps.get(id) ?: Constant.Default.LONG

    @Synchronized
    fun online(id : String) : Boolean = onlines.get(id) ?: Constant.Default.BOOLEAN
}