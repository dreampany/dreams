package com.dreampany.history.data.source.api

import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.model.History

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface HistoryDataSource : DataSource<History> {
    fun getItems(type: HistoryType, day: Int, month: Int): List<History>?
}