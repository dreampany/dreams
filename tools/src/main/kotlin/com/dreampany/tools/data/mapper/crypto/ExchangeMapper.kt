package com.dreampany.tools.data.mapper.crypto

import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.injector.annote.crypto.ExchangeAnnote
import com.dreampany.tools.injector.annote.crypto.ExchangeItemAnnote
import com.dreampany.tools.ui.model.crypto.ExchangeItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ExchangeMapper
@Inject constructor(
    @ExchangeAnnote private val map: SmartMap<String, Exchange>,
    @ExchangeAnnote private val cache: SmartCache<String, Exchange>,
    @ExchangeItemAnnote private val uiMap: SmartMap<String, ExchangeItem>,
    @ExchangeItemAnnote private val uiCache: SmartCache<String, ExchangeItem>
){
    fun getUiItem(id: String): ExchangeItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: ExchangeItem) {
        uiMap.put(id, uiItem)
    }
}