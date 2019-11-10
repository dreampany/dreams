package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Quality
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.ServerAdapter
import com.dreampany.tools.ui.adapter.StationAdapter
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import jp.shts.android.library.TriangleLabelView
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
) : BaseItem< ServerItem.ViewHolder,Server, String>(item, layoutId) {

    companion object {
        fun getItem(item: Server): ServerItem {
            return ServerItem(item, R.layout.item_server)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ServerItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ServerItem.ViewHolder {
        return ServerItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.countryName?.contains(constraint, true) ?: false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private val adapter: ServerAdapter
        private val title: AppCompatTextView
        private val subtitle: AppCompatTextView
        private var label: TriangleLabelView

        init {
            this.adapter = adapter as ServerAdapter
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

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH,T,  S>>
                bind(position: Int, item: I) {
            val uiItem = item as ServerItem
            val item = uiItem.item

            title.text = item.countryName
            subtitle.text = item.id

            var labelTextRes = R.string.low
            var labelColorRes = R.color.material_red500
            if (item.quality == Quality.MEDIUM) {
                labelTextRes = R.string.medium
                labelColorRes = R.color.material_yellow500
            }
            if (item.quality == Quality.HIGH) {
                labelTextRes = R.string.high
                labelColorRes = R.color.material_green500
            }

            label.setPrimaryText(labelTextRes)
            label.setTriangleBackgroundColorResource(labelColorRes)
        }
    }
}