package com.dreampany.hi.data.source.mapper

import com.dreampany.common.data.source.mapper.StoreMapper
import com.dreampany.common.data.source.repo.StoreRepo
import com.dreampany.common.misc.exts.sub
import com.dreampany.common.misc.exts.value
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.UserDataSource
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.network.nearby.model.Peer
import com.google.common.collect.Maps
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/15/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class UserMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: Pref,
    private val gson: Gson
) {
    private val users: MutableMap<String, User>
    private val favorites: MutableMap<String, Boolean>

    init {
        users = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(input: User) = users.put(input.id, input)

    @Throws
    suspend fun isFavorite(input: User): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                Type.USER.value,
                Subtype.DEFAULT.value,
                State.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value
    }

    @Throws
    suspend fun insertFavorite(input: User): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.read(
            input.id,
            Type.USER.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.write(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: User): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.read(
            input.id,
            Type.USER.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.delete(it) }
        return false
    }

    @Throws
    @Synchronized
    suspend fun gets(
        offset: Long,
        limit: Long,
        source: UserDataSource
    ): List<User>? {
        updateCache(source)
        val cache = sort(users.values.toList())
        val result = sub(cache, offset, limit)
        return result
    }

    @Throws
    @Synchronized
    suspend fun read(
        id: String,
        source: UserDataSource
    ): User? {
        updateCache(source)
        val result = users.get(id)
        return result
    }

    @Throws
    @Synchronized
    suspend fun getFavorites(
        source: UserDataSource
    ): List<User>? {
        updateCache(source)
        val stores = storeRepo.reads(
            Type.USER.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> users.get(input.id) }
        var result: List<User>? = null
        outputs?.let {
            result = this.sort(it)
        }
        return result
    }

    @Synchronized
    fun gets(inputs: List<Peer>): List<User> {
        val result = arrayListOf<User>()
        inputs.forEach { input ->
            result.add(read(input))
        }
        return result
    }

    @Synchronized
    fun read(input: Peer): User {
        Timber.v("Resolved User: %s", input.id);
        val id = input.id
        var out: User? = users.get(id)
        if (out == null) {
            out = User(id)
            users.put(id, out)
        }
        parsePearMeta(out, input.meta)
        return out
    }

    private fun parsePearMeta(user: User, meta: ByteArray?) {
        try {
            if (meta == null) throw NullPointerException()
            val json = JsonParser.parseString(String(meta)).asJsonObject
            val name = json.get("name").asString
            user.name = name
        } catch (ignored: Throwable) {
            user.name = null
        }
    }

    fun readUserToByteArray(user: User): ByteArray {
        val json = JsonObject()
        json.addProperty("name", user.name)
        val data = json.toString()
        val array = data.toByteArray()
        return array
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: UserDataSource) {
        if (users.isEmpty()) {
            source.reads()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sort(
        inputs: List<User>
    ): List<User> {
        val temp = ArrayList(inputs)
        //val comparator = CryptoComparator(currency, sort, order)
        //temp.sortWith(comparator)
        return temp
    }

}