package com.dreampany.nearby.data.source.repo

import com.dreampany.framework.inject.annote.Memory
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.nearby.data.model.media.Apk
import com.dreampany.nearby.data.source.api.ApkDataSource
import com.dreampany.nearby.data.source.mapper.ApkMapper
import com.dreampany.nearby.data.source.pref.AppPref
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 29/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ApkRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val mapper: ApkMapper,
    @Memory private val memory: ApkDataSource
) : ApkDataSource {
    override suspend fun isFavorite(input: Apk): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Apk): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<Apk>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Apk): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Apk>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Apk? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Apk>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(ids: List<String>): List<Apk>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<Apk>? {
        TODO("Not yet implemented")
    }
}