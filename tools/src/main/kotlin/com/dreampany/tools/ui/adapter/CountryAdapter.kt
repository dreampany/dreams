package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.CountryItem

/**
 * Created by roman on 2020-01-04
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CountryAdapter(listener: Any? = null) : SmartAdapter<CountryItem>(listener) {

    companion object {
        private val SPAN_COUNT = 2
        private val ITEM_OFFSET = 4
    }

    fun getSpanCount(): Int {
        return CountryAdapter.SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return CountryAdapter.ITEM_OFFSET
    }
}