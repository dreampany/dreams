package com.dreampany.lca.data.source.room

import com.dreampany.lca.data.misc.CoinAlertMapper
import com.dreampany.lca.data.model.CoinAlert
import com.dreampany.lca.data.source.api.CoinAlertDataSource
import com.dreampany.lca.data.source.dao.CoinAlertDao
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomCoinAlertDataSource constructor(val mapper: CoinAlertMapper, val dao : CoinAlertDao) :
    CoinAlertDataSource {

    override fun isExists(id: String): Boolean {
        return dao.getCount(id) > 0
    }


    fun getCount(): Int {
        return dao.count
    }

    fun isExists(coinAlert: CoinAlert): Boolean {
        return dao.getCount(coinAlert.id) > 0
    }

    fun isExistsRx(coinAlert: CoinAlert): Maybe<Boolean> {
        return Maybe.fromCallable { isExists(coinAlert) }
    }

    fun putItem(coinAlert: CoinAlert): Long {
        return dao.insertOrReplace(coinAlert)
    }


    fun delete(coinAlert: CoinAlert): Int {
        return dao.delete(coinAlert)
    }

    fun getItem(id: String): CoinAlert {
        return dao.getItem(id)
    }

    fun getItems(): List<CoinAlert> {
        return dao.items
    }

    fun getItemsRx(): Maybe<List<CoinAlert>> {
        return Maybe.fromCallable { getItems() }
    }

}