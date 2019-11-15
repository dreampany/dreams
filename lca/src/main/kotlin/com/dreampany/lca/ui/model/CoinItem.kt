package com.dreampany.lca.ui.model

import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.ui.view.TextViewClickMovement
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.FrescoUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.util.ViewUtil
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.FrescoUtil
import com.dreampany.lca.R
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.model.Coin
import com.dreampany.lca.misc.Constants
import com.dreampany.lca.misc.CurrencyFormatter
import com.dreampany.lca.ui.adapter.CoinAdapter
import com.dreampany.lca.ui.enums.CoinItemType
import com.facebook.drawee.view.SimpleDraweeView
import com.google.common.base.Objects
import com.like.LikeButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable
import java.util.*

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinItem private constructor(
    item: Coin,
    var currency: Currency,
    var type: CoinItemType,
    var formatter: CurrencyFormatter,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Coin, CoinItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getSimpleItem(coin: Coin, currency: Currency, formatter: CurrencyFormatter): CoinItem {
            return CoinItem(coin, currency, CoinItemType.SIMPLE, formatter, R.layout.item_coin)
        }

        fun getDetailsItem(coin: Coin, currency: Currency, formatter: CurrencyFormatter): CoinItem {
            return CoinItem(coin, currency, CoinItemType.DETAILS, formatter, R.layout.item_coin_details)
        }

        fun getQuoteItem(coin: Coin, currency: Currency, formatter: CurrencyFormatter): CoinItem {
            return CoinItem(coin, currency, CoinItemType.QUOTE, formatter, R.layout.item_coin_quote)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        when (type) {
            CoinItemType.SIMPLE -> return SimpleViewHolder(view, adapter, formatter)
            CoinItemType.DETAILS -> return DetailsViewHolder(view, adapter, formatter)
            CoinItemType.QUOTE -> return QuoteViewHolder(view, adapter, formatter)
            else -> return QuoteViewHolder(view, adapter, formatter)
        }
    }

    override fun filter(constraint: String): Boolean {
        val coin: Coin = item
        val keyword = coin.name + item.symbol + item.slug
        return keyword.toLowerCase().contains(constraint.toLowerCase())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CoinItem
        return Objects.equal(item.item, item) && item.type === type
    }

    abstract class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        val formatter: CurrencyFormatter
    ) : BaseItem.ViewHolder(view, adapter) {

        protected var adapter: CoinAdapter
        val btcFormat: String
        val positiveChange: Int
        val negativeChange: Int

        init {
            this.adapter = adapter as CoinAdapter
            btcFormat = getText(R.string.btc_format)
            positiveChange = R.string.positive_pct_format
            negativeChange = R.string.negative_pct_format
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val coin = uiItem.item

        }

        fun getText(@StringRes resId: Int): String {
            return getContext().getString(resId)
        }

        fun getItemText(@StringRes formatRes: Int, vararg values: Any): String {
            return String.format(getText(formatRes), *values)
        }
    }

    class SimpleViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        formatter: CurrencyFormatter
    ) : ViewHolder(view, adapter, formatter) {

        var icon: SimpleDraweeView
        var name: TextView
        var price: TextView
        var hourChange: TextView
        var dayChange: TextView
        var weekChange: TextView
        var marketCap: TextView
        var dayVolume: TextView
        var lastUpdated: TextView

        var buttonFavorite: LikeButton
        var buttonAlert: LikeButton

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

            buttonFavorite.setOnClickListener(super.adapter.clickListener)
            buttonAlert.setOnClickListener(super.adapter.clickListener)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            val uiItem = item as CoinItem
            val coin = uiItem.item


            val imageUrl = String.format(Locale.ENGLISH, Constants.ImageUrl.CoinMarketCapImageUrl, coin.id)
            FrescoUtil.loadImage(icon, imageUrl, true)
            val nameText = String.format(Locale.ENGLISH, getText(R.string.full_name), coin.symbol, coin.name)
            name.text = nameText

            val currency = uiItem.currency
            val quote = coin.getQuote(currency)

            var price = 0.0
            var hourChange = 0.0
            var dayChange = 0.0
            var weekChange = 0.0
            var marketCap = 0.0
            var dayVolume = 0.0
            if (quote != null) {
                price = quote.price
                hourChange = quote.getHourChange()
                dayChange = quote.getDayChange()
                weekChange = quote.getWeekChange()
                marketCap = quote.getMarketCap()
                dayVolume = quote.getDayVolume()
            }

            this.price.text = formatter.formatPrice(price, uiItem.currency)
            this.marketCap.text = formatter.roundPrice(marketCap, uiItem.currency)
            this.dayVolume.text = formatter.roundPrice(dayVolume, uiItem.currency)

            val hourFormat = if (hourChange >= 0.0f) positiveChange else negativeChange
            val dayFormat = if (dayChange >= 0.0f) positiveChange else negativeChange
            val weekFormat = if (weekChange >= 0.0f) positiveChange else negativeChange

            this.hourChange.text = String.format(getText(hourFormat), hourChange)
            this.dayChange.text = String.format(getText(dayFormat), dayChange)
            this.weekChange.text = String.format(getText(weekFormat), weekChange)

            val startColor = R.color.material_grey400
            val endColor =
                if (hourChange >= 0.0f || dayChange >= 0.0f || weekChange >= 0.0f) R.color.material_green700 else R.color.material_red700

            ViewUtil.blink(this.price, startColor, endColor)

            val hourChangeColor = if (hourChange >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.hourChange.setTextColor(ColorUtil.getColor(getContext(), hourChangeColor))

            val dayChangeColor = if (dayChange >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.dayChange.setTextColor(ColorUtil.getColor(getContext(), dayChangeColor))

            val weekChangeColor = if (weekChange >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.weekChange.setTextColor(ColorUtil.getColor(getContext(), weekChangeColor))

            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                coin.getLastUpdated(),
                TimeUtil.currentTime(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            lastUpdated.text = lastUpdatedTime

            buttonFavorite.setLiked(item.favorite)
            buttonFavorite.tag = coin

            buttonAlert.setLiked(uiItem.alert)
            buttonAlert.tag = coin
        }

    }

    class DetailsViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        formatter: CurrencyFormatter
    ) : ViewHolder(view, adapter, formatter) {

        var icon: SimpleDraweeView
        var name: TextView
        var price: TextView
        var lastUpdated: TextView
        var like: LikeButton

        var marketCap: View
        var volume: View

        var marketCapTitle: TextView
        var marketCapValue: TextView
        var volumeTitle: TextView
        var volumeValue: TextView

        var hourChange: TextView
        var dayChange: TextView
        var weekChange: TextView

        init {
            icon = view.findViewById(R.id.image_icon)
            name = view.findViewById(R.id.text_name)
            price = view.findViewById(R.id.text_price)
            lastUpdated = view.findViewById(R.id.text_last_updated)
            like = view.findViewById(R.id.button_favorite)

            marketCap = view.findViewById(R.id.layout_market_cap)
            volume = view.findViewById(R.id.layout_volume)

            hourChange = view.findViewById(R.id.text_change_1h)
            dayChange = view.findViewById(R.id.text_change_24h)
            weekChange = view.findViewById(R.id.text_change_7d)


            marketCapTitle = marketCap.findViewById(R.id.text_title)
            marketCapValue = marketCap.findViewById(R.id.text_value)
            volumeTitle = volume.findViewById(R.id.text_title)
            volumeValue = volume.findViewById(R.id.text_value)

            like.setOnClickListener(super.adapter.clickListener)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            val uiItem = item as CoinItem
            val coin = uiItem.item

            val imageUrl = String.format(Locale.ENGLISH, Constants.ImageUrl.CoinMarketCapImageUrl, coin.id)
            FrescoUtil.loadImage(icon, imageUrl, true)
            val nameText = String.format(Locale.ENGLISH, getText(R.string.full_name), coin.symbol, coin.name)
            name.text = nameText

            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                coin.getLastUpdated(),
                TimeUtil.currentTime(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            lastUpdated.text = lastUpdatedTime

            val quote = coin.getQuote(item.currency)
            if (quote != null) {
                val price = quote.price
                val hourChange = quote.getHourChange()
                val dayChange = quote.getDayChange()
                val weekChange = quote.getWeekChange()

                val hourFormat = if (hourChange >= 0.0f) positiveChange else negativeChange
                val dayFormat = if (dayChange >= 0.0f) positiveChange else negativeChange
                val weekFormat = if (weekChange >= 0.0f) positiveChange else negativeChange

                this.price.text = formatter.formatPrice(price, uiItem.currency)

                marketCapTitle.setText(R.string.market_cap)
                volumeTitle.setText(R.string.volume_24h)

                val oneHourValue = String.format(getText(hourFormat), hourChange)
                val oneDayValue = String.format(getText(dayFormat), dayChange)
                val weekValue = String.format(getText(weekFormat), weekChange)

                marketCapValue.text = formatter.roundPrice(quote.getMarketCap(), uiItem.currency)
                volumeValue.text = formatter.roundPrice(quote.getDayVolume(), uiItem.currency)

                this.hourChange.text = getItemText(R.string.coin_format, getText(R.string.one_hour), oneHourValue)
                this.dayChange.text = getItemText(R.string.coin_format, getText(R.string.one_day), oneDayValue)
                this.weekChange.text = getItemText(R.string.coin_format, getText(R.string.week), weekValue)

                val change1hColor = if (hourChange >= 0.0f) R.color.material_green700 else R.color.material_red700
                val change24hColor = if (dayChange >= 0.0f) R.color.material_green700 else R.color.material_red700
                val change7dColor = if (weekChange >= 0.0f) R.color.material_green700 else R.color.material_red700

                this.hourChange.setTextColor(ColorUtil.getColor(getContext(), change1hColor))
                this.dayChange.setTextColor(ColorUtil.getColor(getContext(), change24hColor))
                this.weekChange.setTextColor(ColorUtil.getColor(getContext(), change7dColor))
            }

            like.tag = coin
            like.setLiked(item.favorite)
        }
    }

    class QuoteViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        formatter: CurrencyFormatter
    ) : ViewHolder(view, adapter, formatter) {

        var circulatingSupply: View
        var totalSupply: View
        var maxSupply: View

        var circulatingTitle: TextView
        var circulatingValue: TextView
        var totalTitle: TextView
        var totalValue: TextView
        var maxTitle: TextView
        var maxValue: TextView

        var lastUpdated: TextView

        init {
            circulatingSupply = view.findViewById(R.id.layout_circulating)
            totalSupply = view.findViewById(R.id.layout_total)
            maxSupply = view.findViewById(R.id.layout_max)

            circulatingTitle = circulatingSupply.findViewById(R.id.text_title)
            circulatingValue = circulatingSupply.findViewById(R.id.text_value)

            totalTitle = totalSupply.findViewById(R.id.text_title)
            totalValue = totalSupply.findViewById(R.id.text_value)

            maxTitle = maxSupply.findViewById(R.id.text_title)
            maxValue = maxSupply.findViewById(R.id.text_value)

            lastUpdated = view.findViewById(R.id.text_last_updated)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            val uiItem = item as CoinItem
            val coin = uiItem.item

            val symbol = coin.symbol

            val circulating = formatter.roundPrice(coin.getCirculatingSupply()) + " " + symbol
            val total = formatter.roundPrice(coin.getTotalSupply()) + " " + symbol
            val max = formatter.roundPrice(coin.getMaxSupply()) + " " + symbol

            circulatingTitle.setText(R.string.circulating_supply)
            totalTitle.setText(R.string.total_supply)
            maxTitle.setText(R.string.max_supply)

            circulatingValue.text = circulating
            totalValue.text = total
            maxValue.text = max
            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                coin.getLastUpdated(),
                TimeUtil.currentTime(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            lastUpdated.text = lastUpdatedTime
        }
    }
}