package com.dreampany.lca.data.source.api

import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.lca.data.model.Ico
import io.reactivex.Maybe

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface IcoDataSource : DataSource<Ico> {
    fun getLiveItems(limit: Int): List<Ico>?

    fun getLiveItemsRx(limit: Int): Maybe<List<Ico>>

    fun getUpcomingItems(limit: Int): List<Ico>?

    fun getUpcomingItemsRx(limit: Int): Maybe<List<Ico>>

    fun getFinishedItems(limit: Int): List<Ico>?

    fun getFinishedItemsRx(limit: Int): Maybe<List<Ico>>
}