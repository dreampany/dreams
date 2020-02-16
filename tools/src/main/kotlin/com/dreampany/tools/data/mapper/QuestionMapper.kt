package com.dreampany.tools.data.mapper

import android.content.Context
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.injector.annote.coin.CoinAnnote
import com.dreampany.tools.injector.annote.coin.CoinItemAnnote
import com.dreampany.tools.ui.model.CoinItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class QuestionMapper
@Inject constructor(
    private val context: Context,
    @CoinAnnote private val map: SmartMap<String, Coin>,
    @CoinAnnote private val cache: SmartCache<String, Coin>,
    @CoinItemAnnote private val uiMap: SmartMap<String, CoinItem>,
    @CoinItemAnnote private val uiCache: SmartCache<String, CoinItem>
){
}