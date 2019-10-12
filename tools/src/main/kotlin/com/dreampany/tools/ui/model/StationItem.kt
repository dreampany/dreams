package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.StationAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationItem
private constructor(
    item: Station,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Station, StationItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: Station): StationItem {
            return StationItem(item, R.layout.item_station)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): StationItem.ViewHolder {
        return StationItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.id.startsWith(constraint, true)
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private val adapter: StationAdapter
        private val title: AppCompatTextView

        init {
            this.adapter = adapter as StationAdapter
            title = view.findViewById(R.id.view_title)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as StationItem
            val item = uiItem.item
            title.text = item.name
        }
    }
}