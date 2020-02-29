package com.dreampany.framework.data.source.api

/**
 * Created by Roman-372 on 7/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface DataSource<T> {
    fun isEmpty(): Boolean
    fun getCount(): Int
    fun isExists(t: T): Boolean
    fun putItem(t: T): Long
    fun putItems(ts: List<T>): List<Long>?
    fun delete(t: T): Int
    fun delete(ts: List<T>): List<Long>?
    fun getItem(id: String): T?
    fun getItems(): List<T>?
    fun getItems(limit: Long): List<T>?
}