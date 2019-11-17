package com.dreampany.tools.ui.adapter

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.ContactItem
import eu.davidea.flexibleadapter.items.IFlexible
import java.util.*

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ContactAdapter (listener: Any? = null) : SmartAdapter<ContactItem>(listener) {

    override fun addItems(items: List<ContactItem>): Boolean {
        val comparator: Comparator<IFlexible<*>> = Constants.Comparators.Block.getUiComparator()
        as Comparator<IFlexible<*>>
        return super.addItems(items, comparator)
    }
}