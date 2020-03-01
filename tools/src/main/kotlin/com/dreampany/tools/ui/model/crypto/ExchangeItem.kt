package com.dreampany.tools.ui.model.crypto

import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extension.toColor
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.util.CurrencyFormatter
import com.google.android.material.textview.MaterialTextView
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
            return ExchangeItem(item, formatter, R.layout.item_exchange)
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
        return item.market.contains(constraint, true)
    }

    class ViewHolder(
        private val formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : BaseItem.ViewHolder(view, adapter) {

        @StringRes
        protected val btcFormat: Int
        @StringRes
        protected val positiveRatio: Int
        @StringRes
        protected val negativeRatio: Int

        private val market: MaterialTextView
        private val price: MaterialTextView
        private val volume24h: MaterialTextView
        private val change24h: MaterialTextView
        private val changePct24h: MaterialTextView

        init {
            btcFormat = R.string.btc_format
            positiveRatio = R.string.positive_ratio_format
            negativeRatio = R.string.negative_ratio_format

            market = view.findViewById(R.id.text_market)
            price = view.findViewById(R.id.text_price)
            volume24h = view.findViewById(R.id.text_volume_24h)
            change24h = view.findViewById(R.id.text_change_24h)
            changePct24h = view.findViewById(R.id.text_change_pct_24h)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as ExchangeItem
            val item = uiItem.item

            market.text = item.market
            price.text = formatter.format(item.getToSymbol(), item.price)
            volume24h.text = formatter.format(item.getToSymbol(), item.getVolume24h())

            val changePct24hString =
                if (item.getChangePct24h() >= 0.0f) positiveRatio else negativeRatio
            val changePct24hColor =
                if (item.getChangePct24h() >= 0.0f) R.color.material_green500 else R.color.material_red500

            changePct24h.text = getFormattedString(changePct24hString, item.getChangePct24h())
            changePct24h.setTextColor(changePct24hColor.toColor(context))
        }
    }
}