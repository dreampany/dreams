package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.StationAdapter
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import jp.shts.android.library.TriangleLabelView
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as StationItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): StationItem.ViewHolder {
        return StationItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.name?.contains(constraint, true) ?: false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private val adapter: StationAdapter
        private val title: AppCompatTextView
        private val subtitle: AppCompatTextView
        private var label: TriangleLabelView

        init {
            this.adapter = adapter as StationAdapter
            title = view.findViewById(R.id.view_title)
            subtitle = view.findViewById(R.id.view_subtitle)
            label = view.findViewById(R.id.label_type)

            view.setOnClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.OPEN
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as StationItem
            val item = uiItem.item
            title.text = item.name
            subtitle.text = getSubtitle(item)

            if (item.getLastCheckOk()) {
                label.primaryText = getString(R.string.online)
                label.setTriangleBackgroundColorResource(R.color.material_green500)
            } else {
                label.primaryText = getString(R.string.offline)
                label.setTriangleBackgroundColorResource(R.color.material_red500)
            }


            if (adapter.isSelected(uiItem)) {
                title.setTextColor(getColor(R.color.material_black))
            } else {
                title.setTextColor(getColor(R.color.material_grey600))
            }
        }

        private fun getSubtitle(station: Station): String {
            val subtitle = arrayListOf<String>()
            if (!station.getLastCheckOk()) {
                subtitle.add(getString(R.string.station_detail_broken))
            }
            if (station.bitrate > 0) {
                //subtitle.add(getString(R.string.station_detail_bitrate, station.bitrate))
            }
            station.language?.run {
                if (isNotEmpty()) {
                    subtitle.add(this)
                }
            }
            station.state?.run {
                subtitle.add(this)
            }
            return subtitle.joinToString(separator = Constants.Sep.SPACE)
        }
    }
}