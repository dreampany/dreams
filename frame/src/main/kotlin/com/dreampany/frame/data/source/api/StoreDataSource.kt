package com.dreampany.frame.data.source.api

import com.dreampany.frame.data.model.Store
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StoreDataSource : DataSource<Store> {

     fun getItemsOf(type: String, subtype: String, state: String): List<String>

     fun getItemsOfRx(type: String, subtype: String, state: String): Maybe<List<String>>

     fun getItemsOfRx(
        type: String,
        subtype: String,
        state: String,
        limit: Int
    ): Maybe<List<String>>

     fun getItems(type: String, subtype: String, state: String): List<Store>

     fun getItemsRx(type: String, subtype: String, state: String): Maybe<List<Store>>
}