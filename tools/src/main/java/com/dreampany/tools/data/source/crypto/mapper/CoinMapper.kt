package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.extension.isExpired
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.misc.constant.AppConstants
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CoinMapper
@Inject constructor(
    private val pref: CryptoPref
) {
    private val coins: MutableList<Coin>

    init {
        coins = Collections.synchronizedList(ArrayList<Coin>())
    }

    fun isExpired(currency: Currency, sort: CoinSort, order: Order, start: Long): Boolean {
        val time = pref.getExpireTime(currency, sort, order, start)
        return time.isExpired(AppConstants.Times.Crypto.LISTING)
    }

    fun commitExpire(currency: Currency, sort: CoinSort, order: Order, start: Long) {
        pref.commitExpireTime(currency, sort, order, start)
    }
}