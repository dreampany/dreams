package com.dreampany.tube.data.source.repo

import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.framework.inject.annote.Room
import com.dreampany.tube.data.model.Search
import com.dreampany.tube.data.source.api.SearchDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class SearchRepo
@Inject constructor(
    @Room private val room: SearchDataSource,
    @Firestore private val firestore: SearchDataSource
) : SearchDataSource {

    @Throws
    override suspend fun write(input: Search) = withContext(Dispatchers.IO) {
        val success = firestore.write(input)
        room.write(input)
        success
    }

    @Throws
    override suspend fun hit(id: String, ref: String) = withContext(Dispatchers.IO) {
        firestore.hit(id, ref)
    }
}