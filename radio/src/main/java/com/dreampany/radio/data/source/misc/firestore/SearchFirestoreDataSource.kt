package com.dreampany.radio.data.source.misc.firestore

import com.dreampany.radio.data.model.misc.Search
import com.dreampany.radio.data.source.misc.api.SearchDataSource
import com.dreampany.radio.data.source.misc.mapper.SearchMapper
import com.dreampany.radio.manager.AuthManager
import com.dreampany.radio.manager.FirestoreManager
import timber.log.Timber

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SearchFirestoreDataSource(
    private val mapper : SearchMapper,
    private val auth: AuthManager,
    private val firestore: FirestoreManager
) : SearchDataSource {

    @Throws
    override suspend fun write(input: Search): Long {
        try {
            if (auth.signInAnonymously().not()) return -1L
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
            if (auth.signInAnonymously().not()) return -1L
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