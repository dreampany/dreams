package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.tools.data.model.Demo
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.DemoAdapter
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-08-02
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DemoItem
private constructor(
    item: Demo,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Demo, DemoItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: Demo): DemoItem {
            return DemoItem(item)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        val demo = item as DemoItem
        return demo.item.id.contains(constraint, true)
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {

        private var adapter: DemoAdapter

        init {
            this.adapter = adapter as DemoAdapter
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as DemoItem
            val demo = uiItem.item
        }
    }
}