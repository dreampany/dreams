package com.dreampany.tube.data.source.firestore

import com.dreampany.tube.data.model.Search
import com.dreampany.tube.data.source.api.SearchDataSource
import com.dreampany.tube.data.source.mapper.SearchMapper
import timber.log.Timber

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SearchFirestoreDataSource(
    private val mapper : SearchMapper,
    private val firestore: FirestoreManager
) : SearchDataSource {

    @Throws
    override suspend fun write(input: Search): Long {
        try {
            val col = Constants.Keys.SEARCHES
            firestore.write(col, input.id, input)
            return 1
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }

    @Throws
    override suspend fun hit(id: String, ref: String): Long {
        try {
            val col = Constants.Keys.SEARCHES
            val field = Constants.Keys.hit(ref)
            firestore.increment(col, id, field)
            return 1
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }
}