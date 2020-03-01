package com.dreampany.tools.ui.adapter.crypto

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.crypto.ExchangeItem

/**
 * Created by roman on 1/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExchangeAdapter(listener: Any? = null) : SmartAdapter<ExchangeItem>(listener) {

    companion object {
        private val SPAN_COUNT = 2
        private val ITEM_OFFSET = 4
    }

    fun getSpanCount(): Int {
        return ExchangeAdapter.SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return ExchangeAdapter.ITEM_OFFSET
    }
}