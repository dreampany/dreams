package com.dreampany.tools.ui.model

import android.text.format.DateUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.FrescoUtil
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.crypto.CoinAdapter
import com.dreampany.tools.util.CurrencyFormatter
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.textview.MaterialTextView
import com.google.common.base.Objects
import com.like.LikeButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable
import java.util.*

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinItem
private constructor(
    var type: Type,
    var currency: Currency,
    item: Coin,
    var formatter: CurrencyFormatter,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<CoinItem.ViewHolder, Coin, String>(item, layoutId) {

    enum class Type {
        ITEM, INFO, QUOTE
    }

    companion object {
        fun getItem(formatter: CurrencyFormatter, currency: Currency, item: Coin): CoinItem {
            return CoinItem(Type.ITEM, currency, item, formatter, R.layout.item_coin)
        }

        fun getInfoItem(formatter: CurrencyFormatter, currency: Currency, item: Coin): CoinItem {
            return CoinItem(Type.INFO, currency, item, formatter, R.layout.item_coin_info)
        }

        fun getQuoteItem(formatter: CurrencyFormatter, currency: Currency, item: Coin): CoinItem {
            return CoinItem(Type.QUOTE, currency, item, formatter, R.layout.item_coin_quote)
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CoinItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        when (type) {
            Type.ITEM -> return ItemViewHolder(formatter, view, adapter)
            Type.INFO -> return InfoViewHolder(formatter, view, adapter)
            Type.QUOTE -> return QuoteViewHolder(formatter, view, adapter)
            else -> return QuoteViewHolder(formatter, view, adapter)
        }
    }

    override fun filter(constraint: String): Boolean {
        return item.name?.contains(constraint, true) ?: false
    }

    abstract class ViewHolder(
        val formatter: CurrencyFormatter,
        view: View,
        val adapter: CoinAdapter
    ) : BaseItem.ViewHolder(view, adapter) {

    }

    internal class ItemViewHolder(
        formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : ViewHolder(formatter, view, adapter as CoinAdapter) {

        private var icon: SimpleDraweeView
        private var name: MaterialTextView
        private var price: MaterialTextView
        private var hourChange: MaterialTextView
        private var dayChange: MaterialTextView
        private var weekChange: MaterialTextView
        private var marketCap: MaterialTextView
        private var dayVolume: MaterialTextView
        private var lastUpdated: MaterialTextView

        private var buttonFavorite: LikeButton
        private var buttonAlert: LikeButton

        val btcFormat: String
        val positiveChange: Int
        val negativeChange: Int

        init {
            icon = view.findViewById(R.id.image_icon)
            name = view.findViewById(R.id.text_name)
            price = view.findViewById(R.id.text_price)
            hourChange = view.findViewById(R.id.text_change_1h)
            dayChange = view.findViewById(R.id.text_change_24h)
            weekChange = view.findViewById(R.id.text_change_7d)
            marketCap = view.findViewById(R.id.text_market_cap)
            dayVolume = view.findViewById(R.id.text_volume_24h)
            lastUpdated = view.findViewById(R.id.text_last_updated)
            buttonFavorite = view.findViewById(R.id.button_favorite)
            buttonAlert = view.findViewById(R.id.button_alert)

            buttonFavorite.visibility = View.GONE
            buttonAlert.visibility = View.GONE

            btcFormat = getString(R.string.btc_format)
            positiveChange = R.string.positive_pct_format
            negativeChange = R.string.negative_pct_format
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val item = uiItem.item

            val imageUrl =
                String.format(Locale.ENGLISH, Constants.Api.Crypto.CoinMarketCapImageUrl, item.id)
            FrescoUtil.loadImage(icon, imageUrl, true)

            val nameText =
                String.format(Locale.ENGLISH, getString(R.string.full_name), item.symbol, item.name)
            name.text = nameText

            val currency = uiItem.currency
            val quote = item.getQuote(currency)

            var price = 0.0
            var hourChange = 0.0
            var dayChange = 0.0
            var weekChange = 0.0
            var marketCap = 0.0
            var dayVolume = 0.0
            if (quote != null) {
                price = quote.price
                hourChange = quote.getChange1h()
                dayChange = quote.getChange24h()
                weekChange = quote.getChange7d()
                marketCap = quote.getMarketCap()
                dayVolume = quote.getVolume24h()
            }

            this.price.text = formatter.formatPrice(price, uiItem.currency)
            this.marketCap.text = formatter.roundPrice(marketCap, uiItem.currency)
            this.dayVolume.text = formatter.roundPrice(dayVolume, uiItem.currency)

            val hourFormat = if (hourChange >= 0.0f) positiveChange else negativeChange
            val dayFormat = if (dayChange >= 0.0f) positiveChange else negativeChange
            val weekFormat = if (weekChange >= 0.0f) positiveChange else negativeChange

            this.hourChange.text = String.format(getString(hourFormat), hourChange)
            this.dayChange.text = String.format(getString(dayFormat), dayChange)
            this.weekChange.text = String.format(getString(weekFormat), weekChange)

            val startColor = R.color.material_grey400
            val endColor =
                if (hourChange >= 0.0f || dayChange >= 0.0f || weekChange >= 0.0f) R.color.material_green700 else R.color.material_red700


            ViewUtil.blink(this.price, startColor, endColor)

            val hourChangeColor =
                if (hourChange >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.hourChange.setTextColor(ColorUtil.getColor(getContext(), hourChangeColor))

            val dayChangeColor =
                if (dayChange >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.dayChange.setTextColor(ColorUtil.getColor(getContext(), dayChangeColor))

            val weekChangeColor =
                if (weekChange >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.weekChange.setTextColor(ColorUtil.getColor(getContext(), weekChangeColor))

            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                item.getLastUpdated(),
                TimeUtilKt.currentMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            lastUpdated.text = lastUpdatedTime

            //buttonFavorite.setLiked(item.favorite)
            //buttonFavorite.tag = coin

            //buttonAlert.setLiked(uiItem.alert)
            //buttonAlert.tag = coin
        }
    }

    internal class InfoViewHolder(
        formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : ViewHolder(formatter, view, adapter as CoinAdapter) {

        init {

        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val item = uiItem.item

        }
    }

    internal class QuoteViewHolder(
        formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : ViewHolder(formatter, view, adapter as CoinAdapter) {

        init {

        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val item = uiItem.item

        }
    }
}