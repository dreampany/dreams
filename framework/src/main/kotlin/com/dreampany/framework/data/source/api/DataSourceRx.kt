package com.dreampany.framework.data.source.api

import io.reactivex.Maybe

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface DataSourceRx<T> {
    fun isEmptyRx(): Maybe<Boolean>
    fun getCountRx(): Maybe<Int>
    fun isExistsRx(t: T): Maybe<Boolean>
    fun putItemRx(t: T): Maybe<Long>
    fun putItemsRx(ts: List<T>): Maybe<List<Long>>
    fun deleteRx(t: T): Maybe<Int>
    fun deleteRx(ts: List<T>): Maybe<List<Long>>
    fun getItemRx(id: String): Maybe<T>
    fun getItemsRx(): Maybe<List<T>>
    fun getItemsRx(limit: Long): Maybe<List<T>>
}