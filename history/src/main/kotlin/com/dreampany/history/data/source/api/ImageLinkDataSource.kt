package com.dreampany.history.data.source.api

import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.history.data.enums.LinkSource
import com.dreampany.history.data.model.ImageLink
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ImageLinkDataSource : DataSource<ImageLink> {

    fun getItem(source: LinkSource, id: String): ImageLink?

    fun getItemRx(source: LinkSource, id: String): Maybe<ImageLink>

    fun getItems(source: LinkSource, ref: String): List<ImageLink>?

    fun getItemsRx(source: LinkSource, ref: String): Maybe<List<ImageLink>>
}