package com.dreampany.tools.ui.model.crypto

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.util.CurrencyFormatter
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExchangeItem
private constructor(
    item: Exchange,
    var formatter: CurrencyFormatter,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<ExchangeItem.ViewHolder, Exchange, String>(item, layoutId) {

    companion object {
        fun getItem(item: Exchange, formatter: CurrencyFormatter): ExchangeItem {
            return ExchangeItem(item, formatter)
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ExchangeItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(
            formatter,
            view,
            adapter
        )
    }

    override fun filter(constraint: String): Boolean {
        return item.market?.contains(constraint, true) ?: false
    }

    class ViewHolder(
        private val formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : BaseItem.ViewHolder(view, adapter) {

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as ExchangeItem
            val item = uiItem.item

        }
    }
}