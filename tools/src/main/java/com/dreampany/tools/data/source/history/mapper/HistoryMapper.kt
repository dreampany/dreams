package com.dreampany.tools.data.source.history.mapper

import com.dreampany.framework.data.model.Link
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.*
import com.dreampany.tools.api.history.WikiHistory
import com.dreampany.tools.api.history.WikiHistoryData
import com.dreampany.tools.api.history.WikiLink
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.data.model.history.History
import com.dreampany.tools.data.source.history.api.HistoryDataSource
import com.dreampany.tools.data.source.history.pref.HistoryPref
import com.dreampany.tools.misc.constants.HistoryConstants
import com.google.common.collect.Maps
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
class HistoryMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: HistoryPref
) {
    private val histories: MutableMap<String, History>
    private val favorites: MutableMap<String, Boolean>

    init {
        histories = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun isExpired(
        source: HistorySource,
        state: HistoryState,
        month: Int,
        day: Int
    ): Boolean {
        val time = pref.getExpireTime(source, state, month, day)
        return time.isExpired(HistoryConstants.Times.HISTORIES)
    }

    @Synchronized
    fun commitExpire(
        source: HistorySource,
        state: HistoryState,
        month: Int,
        day: Int
    ) = pref.commitExpireTime(source, state, month, day)

    /* @Synchronized
    fun isExpired(id: String, currency: Currency): Boolean {
        val time = pref.getExpireTime(id, currency)
        return time.isExpired(AppConstants.Times.Crypto.COIN)
    }

    @Synchronized
    fun commitExpire(id: String, currency: Currency) =
        pref.commitExpireTime(id, currency)*/


    @Synchronized
    fun add(input: History) = histories.put(input.id, input)

    @Throws
    suspend fun isFavorite(input: History): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                HistoryType.HISTORY.value,
                HistorySubtype.DEFAULT.value,
                HistoryState.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value()
    }

    @Throws
    suspend fun putFavorite(input: History): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.getItem(
            input.id,
            HistoryType.HISTORY.value,
            HistorySubtype.DEFAULT.value,
            HistoryState.FAVORITE.value
        )
        store?.let { storeRepo.insert(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: History): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.getItem(
            input.id,
            HistoryType.HISTORY.value,
            HistorySubtype.DEFAULT.value,
            HistoryState.FAVORITE.value
        )
        store?.let { storeRepo.delete(it) }
        return false
    }

    @Throws
    @Synchronized
    suspend fun getFavorites(
        source: HistoryDataSource
    ): List<History>? {
        updateCache(source)
        val stores = storeRepo.getStores(
            HistoryType.HISTORY.value,
            HistorySubtype.DEFAULT.value,
            HistoryState.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> histories.get(input.id) }
        var result: List<History>? = null
        outputs?.let {
            //result = sortedCoins(it, currency, sort, sortDirection)
        }
        return outputs
    }

    @Synchronized
    fun gets(
        input: WikiHistoryData,
        source: HistorySource,
        state: HistoryState,
        date: String,
        url: String
    ): List<History>? {
        when (state) {
            HistoryState.EVENT -> {
                return gets(input.events, source, state, date, url)
            }
            HistoryState.BIRTH -> {
                return gets(input.births, source, state, date, url)
            }
            HistoryState.DEATH -> {
                return gets(input.deaths, source, state, date, url)
            }
        }
        return null
    }

    @Synchronized
    fun gets(
        inputs: List<WikiHistory>?,
        source: HistorySource,
        state: HistoryState,
        date: String,
        url: String
    ): List<History>? {
        if (inputs.isNullOrEmpty()) return null
        val result = arrayListOf<History>()
        inputs.forEach { rs ->
            result.add(get(rs, source, state, date, url))
        }
        return result
    }

    @Synchronized
    fun get(
        input: WikiHistory,
        source: HistorySource,
        state: HistoryState,
        date: String,
        url: String
    ): History {
        Timber.v("Resolved History: %s", input.text.substring(0, 10))
        val day = date.getDay(HistoryConstants.Date.FORMAT_MONTH_DAY)
        val month = date.getMonth(HistoryConstants.Date.FORMAT_MONTH_DAY)
        val year = input.year.firstPart(Constants.Sep.SPACE)?.toIntOrNull() ?: Constants.Default.INT
        val id = state.value.append(year.toString(), month.toString(), day.toString())
        var out: History? = histories.get(id)
        if (out == null) {
            out = History(id)
            histories.put(id, out)
        }
        out.source = source
        out.state = state
        out.day = day
        out.month = month
        out.year = year
        out.text = input.text
        out.html = input.html
        out.url = url
        out.links = input.links.toLinks()
        return out
    }

    private fun List<WikiLink>.toLinks(): List<Link> = map { Link(it.link, it.title) }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: HistoryDataSource) {
        if (histories.isEmpty()) {
            source.gets()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }
}