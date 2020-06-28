package com.dreampany.nearby.data.source.mapper

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.*
import com.dreampany.nearby.data.enums.State
import com.dreampany.nearby.data.enums.Subtype
import com.dreampany.nearby.data.enums.Type
import com.dreampany.nearby.data.model.User
import com.dreampany.nearby.data.model.media.Apk
import com.dreampany.nearby.data.source.api.ApkDataSource
import com.dreampany.nearby.data.source.api.UserDataSource
import com.dreampany.nearby.data.source.pref.AppPref
import com.dreampany.network.nearby.model.Peer
import com.google.common.collect.Maps
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ApkMapper
@Inject constructor(
    private val context: Context,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: AppPref
) {
    private val apks: MutableMap<String, Apk>
    private val favorites: MutableMap<String, Boolean>

    init {
        apks = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(input: Apk) = apks.put(input.id, input)

    @Throws
    suspend fun isFavorite(input: Apk): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                Type.APK.value,
                Subtype.DEFAULT.value,
                State.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value()
    }

    @Throws
    suspend fun insertFavorite(input: Apk): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.getItem(
            input.id,
            Type.APK.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.insert(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: Apk): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.getItem(
            input.id,
            Type.APK.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.delete(it) }
        return false
    }

    @Throws
    @Synchronized
    suspend fun getItems(
        source: ApkDataSource
    ): List<Apk>? {
        updateCache(source)
        val cache = sort(apks.values.toList())
        //val result = sub(cache, offset, limit)
        return cache
    }

    @Throws
    @Synchronized
    suspend fun get(
        id: String,
        source: ApkDataSource
    ): Apk? {
        updateCache(source)
        val result = apks.get(id)
        return result
    }

    @Throws
    @Synchronized
    suspend fun getFavorites(
        source: ApkDataSource
    ): List<Apk>? {
        updateCache(source)
        val stores = storeRepo.getStores(
            Type.APK.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> apks.get(input.id) }
        var result: List<Apk>? = null
        outputs?.let {
            result = this.sort(it)
        }
        return result
    }

    @Synchronized
    fun getItems(inputs: List<ApplicationInfo>, pm: PackageManager): List<Apk> {
        val result = arrayListOf<Apk>()
        inputs.forEach { input ->
            result.add(get(input, pm))
        }
        return result
    }

    @Synchronized
    fun get(input: ApplicationInfo, pm: PackageManager): Apk {
        Timber.v("Resolved Apk: %s", input.packageName);
        val id = input.packageName
        var out: Apk? = apks.get(id)
        if (out == null) {
            out = Apk(id)
            apks.put(id, out)
        }
        out.displayName = input.loadLabel(pm).toString()
        out.uri = input.publicSourceDir
        out.thumbUri = context.appIconUri(id)?.toString()
        out.mimeType = input.publicSourceDir.mimeType
        out.size = input.publicSourceDir.fileSize
        out.versionCode = context.versionCode(id)
        out.versionName = context.versionName(id)
        out.isSystem = input.isSystemApp
        return out
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: ApkDataSource) {
        if (apks.isEmpty()) {
            source.gets()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sort(
        inputs: List<Apk>
    ): List<Apk> {
        val temp = ArrayList(inputs)
        //val comparator = CryptoComparator(currency, sort, order)
        //temp.sortWith(comparator)
        return temp
    }

}