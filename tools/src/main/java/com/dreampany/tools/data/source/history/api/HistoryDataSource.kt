package com.dreampany.tools.data.source.history.api

import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.data.model.history.History

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface HistoryDataSource {

    @Throws
    suspend fun isFavorite(input: History): Boolean

    @Throws
    suspend fun toggleFavorite(input: History): Boolean

    @Throws
    suspend fun getFavorites(): List<History>?

    @Throws
    suspend fun put(input: History): Long

    @Throws
    suspend fun put(inputs: List<History>): List<Long>?

    @Throws
    suspend fun get(id: String): History?

    @Throws
    suspend fun gets(): List<History>?

    @Throws
    suspend fun gets(ids: List<String>): List<History>?

    @Throws
    suspend fun gets(source: HistorySource,
                     type: HistoryType,
                     subtype: HistorySubtype,
                     month: Int,
                     day: Int): List<History>?

}