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

    fun getCount(id: String, type: String, subtype: String): Int

    fun getCountRx(id: String, type: String, subtype: String): Maybe<Int>

    fun getCountByType(type: String, subtype: String, state: String): Int

    fun getCountByTypeRx(type: String, subtype: String, state: String): Maybe<Int>

    fun getItem(id: String, type: String, subtype: String, state: String): Store?

    fun getItemRx(id: String, type: String, subtype: String, state: String): Maybe<Store>

}