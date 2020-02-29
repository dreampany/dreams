package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Country
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.CountryAdapter
import com.google.common.base.Objects
import com.haipq.android.flagkit.FlagImageView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2020-01-04
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CountryItem
private constructor(
    item: Country,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<CountryItem.ViewHolder, Country, String>(item, layoutId) {

    var count: Int = Constants.Default.INT

    companion object {
        fun getItem(item: Country): CountryItem {
            return CountryItem(item, R.layout.item_country)
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CountryItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): CountryItem.ViewHolder {
        return CountryItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.name?.contains(constraint, true) ?: false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private val adapter: CountryAdapter
        private val flag: FlagImageView
        private val title: AppCompatTextView
        private val count: AppCompatTextView

        init {
            this.adapter = adapter as CountryAdapter
            flag = view.findViewById(R.id.view_flag)
            title = view.findViewById(R.id.view_title)
            count = view.findViewById(R.id.view_count)

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
            val uiItem = item as CountryItem
            val item = uiItem.item

            flag.countryCode = item.id
            title.text = item.name
            count.text = context.getString(R.string.servers_count, uiItem.count)
        }
    }
}