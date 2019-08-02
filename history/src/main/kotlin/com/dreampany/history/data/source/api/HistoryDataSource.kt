package com.dreampany.history.data.source.api

import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.model.History
import io.reactivex.Maybe

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface HistoryDataSource : DataSource<History> {

    fun getItems(source: HistorySource, type: HistoryType, day: Int, month: Int): List<History>?

    fun getItemsRx(
        source: HistorySource,
        type: HistoryType,
        day: Int,
        month: Int
    ): Maybe<List<History>>
}