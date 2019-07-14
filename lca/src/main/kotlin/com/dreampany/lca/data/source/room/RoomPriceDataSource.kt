package com.dreampany.lca.data.source.room

import com.dreampany.lca.data.misc.PriceMapper
import com.dreampany.lca.data.model.Price
import com.dreampany.lca.data.source.api.PriceDataSource
import com.dreampany.lca.data.source.dao.PriceDao
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomPriceDataSource constructor(
    val mapper: PriceMapper,
    val dao: PriceDao
) : PriceDataSource {

    override fun isEmpty(): Boolean {
        return getCount() == 0
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable({ this.isEmpty() })
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        return Maybe.empty()
    }

    override fun isExists(price: Price): Boolean {
        return dao.getCount(price.id) > 0
    }

    override fun isExistsRx(price: Price): Maybe<Boolean> {
        return Maybe.fromCallable { isExists(price) }
    }

    override fun putItem(price: Price): Long {
        return dao.insertOrReplace(price)
    }

    override fun putItemRx(price: Price): Maybe<Long> {
        return Maybe.fromCallable { dao.insertOrReplace(price) }
    }

    override fun putItems(prices: List<Price>): List<Long>? {
        return null
    }

   override fun putItemsRx(prices: List<Price>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun delete(price: Price): Int {
        return 0
    }

    override fun deleteRx(price: Price): Maybe<Int> {
        return Maybe.empty()
    }

   override fun delete(prices: List<Price>): List<Long>? {
        return null
    }

   override fun deleteRx(prices: List<Price>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItems(): List<Price>? {
        return null
    }

    override fun getItemsRx(): Maybe<List<Price>> {
        return Maybe.empty()
    }

    override fun getItems(limit: Int): List<Price>? {
        return null
    }

    override fun getItemsRx(limit: Int): Maybe<List<Price>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): Price? {
        return null
    }

    override fun getItemRx(id: String): Maybe<Price> {
        return Maybe.empty()
    }
}