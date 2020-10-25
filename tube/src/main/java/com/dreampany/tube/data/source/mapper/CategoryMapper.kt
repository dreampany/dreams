package com.dreampany.tube.data.source.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.exts.value
import com.dreampany.tube.api.model.CategorySnippet
import com.dreampany.tube.api.model.VideoCategory
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.source.api.CategoryDataSource
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.misc.Constants
import com.google.common.collect.Maps
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CategoryMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: Prefs
) {
    private val categories: MutableMap<String, Category>
    private val favorites: MutableMap<String, Boolean>
    private val countries: HashSet<String>

    init {
        categories = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
        countries = HashSet(Locale.getISOCountries().asList())
    }

    fun isIsoCountry(country: String): Boolean = countries.contains(country)

    val isExpired: Boolean
        get() {
            val time = pref.getExpireTimeOfCategory()
            return time.isExpired(Constants.Times.CATEGORIES)
        }

    @Synchronized
    fun commitExpire() = pref.commitExpireTimeOfCategory()

    @Synchronized
    fun add(input: Category) = categories.put(input.id, input)

    @Throws
    suspend fun isFavorite(input: Category): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                Type.CATEGORY.value,
                Subtype.DEFAULT.value,
                State.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value
    }

    @Throws
    suspend fun insertFavorite(input: Category): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.readStore(
            input.id,
            Type.CATEGORY.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.write(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: Category): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.readStore(
            input.id,
            Type.CATEGORY.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.delete(it) }
        return false
    }

    @Throws
    @Synchronized
    suspend fun gets(
        source: CategoryDataSource
    ): List<Category>? {
        updateCache(source)
        val cache = sort(categories.values.toList())
        //val result = sub(cache )
        return cache
    }

    @Throws
    @Synchronized
    suspend fun get(
        id: String,
        source: CategoryDataSource
    ): Category? {
        updateCache(source)
        val result = categories.get(id)
        return result
    }

    @Throws
    @Synchronized
    suspend fun getFavorites(
        source: CategoryDataSource
    ): List<Category>? {
        updateCache(source)
        val stores = storeRepo.reads(
            Type.CATEGORY.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> categories.get(input.id) }
        var result: List<Category>? = null
        outputs?.let {
            result = this.sort(it)
        }
        return result
    }

    @Synchronized
    fun gets(inputs: List<VideoCategory>): List<Category> {
        val result = arrayListOf<Category>()
        inputs.forEach { input ->
            result.add(get(input))
        }
        return result
    }

    @Synchronized
    fun get(input: VideoCategory): Category {
        Timber.v("Resolved Category: %s", input.id);
        val id = input.id
        var output: Category? = categories.get(id)
        if (output == null) {
            output = Category(id)
            categories.put(id, output)
        }
        bindSnippet(input.snippet, output)
        return output
    }

    private fun bindSnippet(input: CategorySnippet, output: Category) {
        output.channelId = input.channelId
        output.title = input.title
        output.assignable = input.assignable
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: CategoryDataSource) {
        if (categories.isEmpty()) {
            source.reads()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sort(
        inputs: List<Category>
    ): List<Category> {
        val temp = ArrayList(inputs)
        //val comparator = CryptoComparator(currency, sort, order)
        //temp.sortWith(comparator)
        return temp
    }

}