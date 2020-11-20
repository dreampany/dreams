package com.dreampany.tube.data.source.mapper

import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.tube.api.model.*
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.misc.Constants
import com.google.common.collect.Maps
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    private val timeRepo: TimeRepo,
    private val pref: Prefs,
    private val gson: Gson
) {

    private val type = object : TypeToken<List<Video>>() {}.type
    private val videos: MutableMap<String, MutableMap<String, Video>>
    private val favorites: MutableMap<String, Boolean>

    init {
        videos = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun writeExpire(categoryId: String, offset: Long) =
        pref.writeExpireTimeOfCategoryId(categoryId, offset)

    @Synchronized
    fun isExpired(categoryId: String, offset: Long): Boolean {
        val time = pref.readExpireTimeOfCategoryId(categoryId, offset)
        return time.isExpired(Constants.Times.VIDEOS)
    }

    @Throws
    suspend fun writeExpire(id: String) {
        val time = Time(id, Type.VIDEO.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        timeRepo.write(time)
    }

    @Throws
    suspend fun isExpired(id: String): Boolean {
        val time =
            timeRepo.readTime(id, Type.VIDEO.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        return time.isExpired(Constants.Times.VIDEOS)
    }

    @Throws
    suspend fun commitExpireOfRelated(id: String) {
        val time = Time(id, Type.VIDEO.value, Subtype.DEFAULT.value, State.RELATED.value)
        timeRepo.write(time)
    }

    @Throws
    suspend fun isExpiredOfRelated(id: String): Boolean {
        val time =
            timeRepo.readTime(id, Type.VIDEO.value, Subtype.DEFAULT.value, State.RELATED.value)
        return time.isExpired(Constants.Times.VIDEOS)
    }

    fun setRegionVideos(regionCode: String, videos: List<Video>) {
        val json = videos.toJson
        pref.setPrivately(Constants.Keys.Pref.VIDEOS.plus(regionCode), json)
    }

    fun getRegionVideos(regionCode: String): List<Video>? {
        val json = pref.getPrivately(
            Constants.Keys.Pref.VIDEOS.plus(regionCode),
            Constant.Default.STRING
        )
        return if (json.isNullOrEmpty()) null else json.toItems
    }

    fun setEventVideos(eventType: String, videos: List<Video>) {
        val json = videos.toJson
        pref.setPrivately(Constants.Keys.Pref.VIDEOS.plus(eventType), json)
    }

    fun getEventVideos(eventType: String): List<Video>? {
        val json = pref.getPrivately(
            Constants.Keys.Pref.VIDEOS.plus(eventType),
            Constant.Default.STRING
        )
        return if (json.isNullOrEmpty()) null else json.toItems
    }


    @Synchronized
    fun write(input: Video) {
        val categoryId = input.categoryId ?: return
        if (!videos.containsKey(categoryId)) {
            videos[categoryId] = Maps.newConcurrentMap()
        }
        videos[categoryId]?.put(input.id, input)
    }

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
        return favorites.get(input.id).value
    }

    @Throws
    suspend fun insertFavorite(input: Video): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.readStore(
            input.id,
            Type.VIDEO.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.write(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: Video): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.readStore(
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
    suspend fun reads(
        categoryId: String,
        offset: Long,
        limit: Long,
        source: VideoDataSource
    ): List<Video>? {
        updateCache(categoryId, source)
        val values = videos[categoryId]?.values?.toList() ?: return null
        val cache = sort(values)
        val result = sub(cache, offset, limit)
        return result
    }

    @Throws
    @Synchronized
    suspend fun read(
        id: String,
        source: VideoDataSource
    ): Video? {
        /*updateCache(source)
        val result = videos.get(id)
        return result*/
        return null
    }

    @Throws
    @Synchronized
    suspend fun favorites(
        source: VideoDataSource
    ): List<Video>? {
        //updateCache(source)
        val stores = storeRepo.reads(
            Type.VIDEO.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> source.get(input.id) }
        var result: List<Video>? = null
        outputs?.let {
            result = this.sort(it)
        }
        return result
    }

    @Synchronized
    fun getsOfSearch(categoryId: String, inputs: List<SearchResult>): List<Video> {
        val result = arrayListOf<Video>()
        inputs.forEach { input ->
            result.add(read(categoryId, input))
        }
        return result
    }

    @Synchronized
    fun getsOfSearch(inputs: List<SearchResult>): List<Video> {
        val result = arrayListOf<Video>()
        inputs.forEach { input ->
            result.add(read(input))
        }
        return result
    }

    @Synchronized
    fun reads(inputs: List<VideoResult>): List<Video> {
        val result = arrayListOf<Video>()
        inputs.forEach { input ->
            result.add(read(input))
        }
        return result
    }

    @Synchronized
    fun read(categoryId: String, input: SearchResult): Video {
        Timber.v("Resolved Video: %s", input.id)
        val id = input.id.videoId
        var output: Video? = videos[categoryId]?.get(id)
        if (output == null) {
            output = Video(id)
            write(output)
        }
        output.categoryId = categoryId
        bindSnippet(input.snippet, output)
        return output
    }

    @Synchronized
    fun read(input: SearchResult): Video {
        Timber.v("Resolved Video: %s", input.id)
        val id = input.id.videoId
        var output: Video? = null
        if (output == null) {
            output = Video(id)
            write(output)
        }
        //output.categoryId = categoryId
        bindSnippet(input.snippet, output)
        return output
    }

    @Synchronized
    fun read(input: VideoResult): Video {
        Timber.v("Resolved Video: %s", input.id)
        val categoryId = input.snippet.categoryId
        val id = input.id
        var output: Video? = videos[categoryId]?.get(id)
        if (output == null) {
            output = Video(id)
            write(output)
        }
        bindSnippet(input.snippet, output)
        bindContentDetails(input.contentDetails, output)
        bindStatistics(input.statistics, output)
        return output
    }

    private fun bindSnippet(input: SearchSnippet, output: Video) {
        output.title = input.title
        output.description = input.description
        output.channelId = input.channelId
        output.channelTitle = input.channelTitle
        output.thumbnail = input.thumbnails.values.lastOrNull()?.url
        output.liveBroadcastContent = input.liveBroadcastContent
        output.publishedAt = input.publishedAt.simpleUtc
    }

    private fun bindSnippet(input: VideoSnippet, output: Video) {
        output.title = input.title
        output.description = input.description
        output.channelId = input.channelId
        output.channelTitle = input.channelTitle
        output.categoryId = input.categoryId
        output.thumbnail = input.thumbnails.values.lastOrNull()?.url
        output.tags = input.tags
        output.liveBroadcastContent = input.liveBroadcastContent
        output.publishedAt = input.publishedAt.simpleUtc
    }

    private fun bindContentDetails(input: ContentDetails, output: Video) {
        output.duration = input.duration.isoTime
        output.dimension = input.dimension
        output.definition = input.definition
        output.licensedContent = input.licensedContent
    }

    private fun bindStatistics(input: Statistics, output: Video) {
        output.viewCount = input.viewCount
        output.likeCount = input.likeCount
        output.dislikeCount = input.dislikeCount
        output.favoriteCount = input.favoriteCount
        output.commentCount = input.commentCount
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(categoryId: String, source: VideoDataSource) {
        if (videos.get(categoryId).isNullOrEmpty()) {
            source.getsOfCategoryId(categoryId)?.let {
                if (it.isNotEmpty())
                    it.forEach { write(it) }
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

    private val List<Video>.toJson: String
        get() = gson.toJson(this, type)

    private val String.toItems: List<Video>
        get() = gson.fromJson<List<Video>>(this, type)

}