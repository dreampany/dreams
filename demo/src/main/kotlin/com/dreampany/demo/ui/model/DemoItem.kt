package com.dreampany.demo.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.demo.data.model.Demo
import com.dreampany.demo.ui.adapter.DemoAdapter
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.ui.model.BaseItemKt
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DemoItem private constructor(item: Demo, @LayoutRes layoutId: Int = 0) :
    BaseItemKt<Demo, DemoItem.ViewHolder, String>(item, layoutId) {

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
        return item.id.startsWith(constraint, true);
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItemKt.ViewHolder(view, adapter) {

        private var adapter: DemoAdapter

        init {
            this.adapter = adapter as DemoAdapter
        }

        override fun <VH : BaseItemKt.ViewHolder, T : BaseKt, S : Serializable, I : BaseItemKt<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as DemoItem
            val word = uiItem.item
        }
    }
}