package com.dreampany.history.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.history.ui.model.ImageLinkItem

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ImageLinkAdapter(listener: Any? = null) : SmartAdapter<ImageLinkItem>(listener) {

    companion object {
        val SPAN_COUNT = 2
        val ITEM_OFFSET = 4
    }

    fun getSpanCount() : Int {
        return SPAN_COUNT
    }

    fun getItemOffset() : Int {
        return ITEM_OFFSET
    }
}