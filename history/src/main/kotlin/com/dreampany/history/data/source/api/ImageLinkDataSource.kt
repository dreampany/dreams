package com.dreampany.history.data.source.api

import com.dreampany.frame.data.model.ImageLink
import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.history.data.enums.HistorySource
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ImageLinkDataSource : DataSource<ImageLink> {

    fun getItem(source: HistorySource, id: String): ImageLink?

    fun getItemRx(source: HistorySource, ref: String, url: String): Maybe<ImageLink>

    fun getItems(source: HistorySource, ref: String): List<ImageLink>?

    fun getItemsRx(source: HistorySource, ref: String): Maybe<List<ImageLink>>
}