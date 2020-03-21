package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
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

}