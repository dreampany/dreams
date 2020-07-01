package com.dreampany.tube.data.source.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.sub
import com.dreampany.framework.misc.exts.value
import com.dreampany.tube.api.model.VideoResult
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.pref.AppPref
import com.google.common.collect.Maps
import com.google.gson.Gson
import com.google.gson.JsonObject
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
class VideoMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: AppPref,
    private val gson: Gson
) {
    private val videos: MutableMap<String, Video>
    private val favorites: MutableMap<String, Boolean>

    init {
        videos = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(input: Video) = videos.put(input.id, input)

    @Throws
    suspend fun isFavorite(input: Video): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                Type.VIDEO.value,
                Subtype.DEFAULT.value,
                State.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value()
    }

    @Throws
    suspend fun insertFavorite(input: Video): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.getItem(
            input.id,
            Type.VIDEO.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.insert(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: Video): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.getItem(
            input.id,
            Type.VIDEO.value,
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
        source: VideoDataSource
    ): List<Video>? {
        updateCache(source)
        val cache = sort(videos.values.toList())
        val result = sub(cache, offset, limit)
        return result
    }

    @Throws
    @Synchronized
    suspend fun get(
        id: String,
        source: VideoDataSource
    ): Video? {
        updateCache(source)
        val result = videos.get(id)
        return result
    }

    @Throws
    @Synchronized
    suspend fun getFavorites(
        source: VideoDataSource
    ): List<Video>? {
        updateCache(source)
        val stores = storeRepo.getStores(
            Type.VIDEO.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> videos.get(input.id) }
        var result: List<Video>? = null
        outputs?.let {
            result = this.sort(it)
        }
        return result
    }

    @Synchronized
    fun gets(inputs: List<VideoResult>): List<Video> {
        val result = arrayListOf<Video>()
        inputs.forEach { input ->
            result.add(get(input))
        }
        return result
    }

    @Synchronized
    fun get(input: VideoResult): Video {
        Timber.v("Resolved Video: %s", input.id);
        val id = input.id
        var out: Video? = videos.get(id)
        if (out == null) {
            out = Video(id)
            videos.put(id, out)
        }

        return out
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: VideoDataSource) {
        if (videos.isEmpty()) {
            source.gets()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sort(
        inputs: List<Video>
    ): List<Video> {
        val temp = ArrayList(inputs)
        //val comparator = CryptoComparator(currency, sort, order)
        //temp.sortWith(comparator)
        return temp
    }

}