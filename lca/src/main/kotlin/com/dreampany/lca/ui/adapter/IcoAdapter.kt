package com.dreampany.lca.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.lca.ui.model.CoinItem
import com.dreampany.lca.ui.model.IcoItem


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class IcoAdapter(listener: Any) : SmartAdapter<IcoItem>(listener) {

    override fun addItems(items: List<IcoItem>): Boolean {
        if (isEmpty) {
            return super.addItems(items);
        }
        for (item in items) {
            addItem(item)
        }
        return true
    }
}