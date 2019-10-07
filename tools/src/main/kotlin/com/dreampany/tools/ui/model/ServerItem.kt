package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.misc.Constants
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ServerItem
private constructor(
    item: Server,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Server, ServerItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: Server): ServerItem {
            return ServerItem(item, 0)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ServerItem.ViewHolder {
        return ServerItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.id.startsWith(constraint, true)
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {


        init {

        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as ServerItem
            val item = uiItem.item

        }
    }
}