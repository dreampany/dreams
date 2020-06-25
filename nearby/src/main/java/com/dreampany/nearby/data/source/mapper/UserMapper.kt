package com.dreampany.nearby.data.source.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.sub
import com.dreampany.framework.misc.exts.value
import com.dreampany.nearby.data.enums.State
import com.dreampany.nearby.data.enums.Subtype
import com.dreampany.nearby.data.enums.Type
import com.dreampany.nearby.data.model.User
import com.dreampany.nearby.data.source.api.UserDataSource
import com.dreampany.nearby.data.source.pref.AppPref
import com.dreampany.network.nearby.model.Peer
import com.google.common.collect.Maps
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class UserMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: AppPref,
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
        return favorites.get(input.id).value()
    }

    @Throws
    suspend fun insertFavorite(input: User): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.getItem(
            input.id,
            Type.USER.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.insert(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: User): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.getItem(
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
    suspend fun getItems(
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
    suspend fun get(
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
        val stores = storeRepo.getStores(
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
    fun getItems(inputs: List<Peer>): List<User> {
        val result = arrayListOf<User>()
        inputs.forEach { input ->
            result.add(get(input))
        }
        return result
    }

    @Synchronized
    fun get(input: Peer): User {
        Timber.v("Resolved User: %s", input.id);
        val id = input.id
        var out: User? = users.get(id)
        if (out == null) {
            out = User(id)
            users.put(id, out)
        }
        parseUserData(out, input.meta)
        return out
    }

    fun parseUserData(user: User, data: ByteArray?) {
        val parser = JsonParser()
        try {
            val json = parser.parse(data.toString()).asJsonObject
            val name = json.get("name").asString
            user.name = name
        } catch (ignored : JsonParseException) {
            user.name = null
        }
    }

    fun getUserData(user: User): ByteArray {
        val json = JsonObject()
        json.addProperty("name", user.name)
        return json.toString().toByteArray()
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: UserDataSource) {
        if (users.isEmpty()) {
            source.gets()?.let {
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