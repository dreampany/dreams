package com.dreampany.tools.ui.model.crypto

import android.text.format.DateUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extensions.gone
import com.dreampany.framework.misc.extensions.setOnSafeClickListener
import com.dreampany.framework.misc.extensions.setUrl
import com.dreampany.framework.misc.extensions.toColor
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
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
        fun getItem(currency: Currency, item: Coin, formatter: CurrencyFormatter): CoinItem {
            return CoinItem(
                Type.ITEM,
                currency,
                item,
                formatter,
                R.layout.item_coin
            )
        }

        fun getInfoItem(currency: Currency, item: Coin, formatter: CurrencyFormatter): CoinItem {
            return CoinItem(
                Type.INFO,
                currency,
                item,
                formatter,
                R.layout.item_coin_info
            )
        }

        fun getQuoteItem(currency: Currency, item: Coin, formatter: CurrencyFormatter): CoinItem {
            return CoinItem(
                Type.QUOTE,
                currency,
                item,
                formatter,
                R.layout.item_coin_quote
            )
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id, type)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CoinItem
        return Objects.equal(this.item.id, item.item.id) && item.type == type
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        when (type) {
            Type.ITEM -> return ItemViewHolder(
                formatter,
                view,
                adapter
            )
            Type.INFO -> return InfoViewHolder(
                formatter,
                view,
                adapter
            )
            Type.QUOTE -> return QuoteViewHolder(
                formatter,
                view,
                adapter
            )
            else -> return QuoteViewHolder(
                formatter,
                view,
                adapter
            )
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

        @StringRes
        protected val btcFormat: Int
        @StringRes
        protected val positiveRatio: Int
        @StringRes
        protected val negativeRatio: Int

        init {
            btcFormat = R.string.btc_format
            positiveRatio = R.string.positive_ratio_format
            negativeRatio = R.string.negative_ratio_format
        }
    }

    internal class ItemViewHolder(
        formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : ViewHolder(formatter, view, adapter as CoinAdapter) {

        private val icon: SimpleDraweeView
        private val textName: MaterialTextView
        private val textPrice: MaterialTextView
        private val textChange1h: MaterialTextView
        private val textChange24h: MaterialTextView
        private val textChange7d: MaterialTextView
        private val textMarketCap: MaterialTextView
        private val textVolume24h: MaterialTextView
        private val textLastUpdated: MaterialTextView

        private val buttonFavorite: LikeButton
        private val buttonAlert: LikeButton

        init {
            icon = view.findViewById(R.id.image_icon)
            textName = view.findViewById(R.id.text_market)
            textPrice = view.findViewById(R.id.text_price)
            textChange1h = view.findViewById(R.id.text_change_1h)
            textChange24h = view.findViewById(R.id.text_change_24h)
            textChange7d = view.findViewById(R.id.text_change_7d)
            textMarketCap = view.findViewById(R.id.text_market_cap)
            textVolume24h = view.findViewById(R.id.text_volume_24h)
            textLastUpdated = view.findViewById(R.id.text_last_updated)
            buttonFavorite = view.findViewById(R.id.button_favorite)
            buttonAlert = view.findViewById(R.id.button_alert)

            buttonFavorite.gone()
            buttonAlert.gone()

            view.setOnSafeClickListener {
                this.adapter.getItem(adapterPosition)?.let {
                    this.adapter.uiItemClickListener?.onUiItemClick(
                        view = view,
                        item = it,
                        action = Action.VIEW
                    )
                }
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val item = uiItem.item

            val imageUrl =
                String.format(Locale.ENGLISH, Constants.Api.Crypto.CoinMarketCapImageUrl, item.id)
            icon.setUrl(imageUrl)

            val nameText =
                String.format(
                    Locale.ENGLISH,
                    getString(R.string.crypto_symbol_name),
                    item.symbol,
                    item.name
                )
            textName.text = nameText

            val currency = uiItem.currency
            val quote = item.getQuote(currency)

            var price = 0.0
            var change1h = 0.0
            var change24h = 0.0
            var change7d = 0.0
            var marketCap = 0.0
            var volume24h = 0.0
            if (quote != null) {
                price = quote.price
                change1h = quote.getChange1h()
                change24h = quote.getChange24h()
                change7d = quote.getChange7d()
                marketCap = quote.getMarketCap()
                volume24h = quote.getVolume24h()
            }

            this.textPrice.text = formatter.formatPrice(price, uiItem.currency)
            this.textMarketCap.text = formatter.roundPrice(marketCap, uiItem.currency)
            this.textVolume24h.text = formatter.roundPrice(volume24h, uiItem.currency)

            val change1hFormat = if (change1h >= 0.0f) positiveRatio else negativeRatio
            val change24hFormat = if (change24h >= 0.0f) positiveRatio else negativeRatio
            val change7dFormat = if (change7d >= 0.0f) positiveRatio else negativeRatio

            this.textChange1h.text = getFormattedString(change1hFormat, change1h)
            this.textChange24h.text = getFormattedString(change24hFormat, change24h)
            this.textChange7d.text = getFormattedString(change7dFormat, change7d)

            val startColor = R.color.material_grey400
            val endColor =
                if (change1h >= 0.0f || change24h >= 0.0f || change7d >= 0.0f) R.color.material_green700 else R.color.material_red700

            ViewUtil.blink(this.textPrice, startColor, endColor)

            val hourChangeColor =
                if (change1h >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.textChange1h.setTextColor(getColor(hourChangeColor))

            val dayChangeColor =
                if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.textChange24h.setTextColor(getColor(dayChangeColor))

            val weekChangeColor =
                if (change7d >= 0.0f) R.color.material_green700 else R.color.material_red700
            this.textChange7d.setTextColor(getColor(weekChangeColor))

            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                item.getLastUpdated(),
                TimeUtilKt.currentMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            textLastUpdated.text = lastUpdatedTime

            buttonFavorite.setLiked(uiItem.favorite)
            buttonFavorite.tag = item

            buttonAlert.setLiked(uiItem.alert)
            buttonAlert.tag = item
        }
    }

    internal class InfoViewHolder(
        formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : ViewHolder(formatter, view, adapter as CoinAdapter) {

        private val icon: SimpleDraweeView
        private val name: MaterialTextView
        private val price: MaterialTextView
        private val lastUpdated: MaterialTextView
        private val like: LikeButton

        private val marketCap: View
        private val volume: View

        private val marketCapTitle: MaterialTextView
        private val marketCapValue: MaterialTextView
        private val volumeTitle: MaterialTextView
        private val volumeValue: MaterialTextView

        private val hourChange: MaterialTextView
        private val dayChange: MaterialTextView
        private val weekChange: MaterialTextView

        init {
            icon = view.findViewById(R.id.image_icon)
            name = view.findViewById(R.id.text_market)
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

            like.setOnSafeClickListener {
                super.adapter.clickListener?.onClick(it)
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val item = uiItem.item

            val imageUrl =
                String.format(Locale.ENGLISH, Constants.Api.Crypto.CoinMarketCapImageUrl, item.id)
            icon.setUrl(imageUrl)

            val nameText =
                String.format(
                    Locale.ENGLISH,
                    getString(R.string.crypto_symbol_name),
                    item.symbol,
                    item.name
                )
            name.text = nameText

            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                item.getLastUpdated(),
                TimeUtilKt.currentMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            lastUpdated.text = lastUpdatedTime

            val quote = item.getQuote(uiItem.currency)
            if (quote != null) {
                val price = quote.price
                val change1h = quote.getChange1h()
                val change24h = quote.getChange24h()
                val change7d = quote.getChange7d()

                val hourFormat = if (change1h >= 0.0f) positiveRatio else negativeRatio
                val dayFormat = if (change24h >= 0.0f) positiveRatio else negativeRatio
                val weekFormat = if (change7d >= 0.0f) positiveRatio else negativeRatio

                this.price.text = formatter.formatPrice(price, uiItem.currency)

                marketCapTitle.setText(R.string.market_cap)
                volumeTitle.setText(R.string.volume_24h)

                val oneHourValue = getFormattedString(hourFormat, change1h)
                val oneDayValue = getFormattedString(dayFormat, change24h)
                val weekValue = getFormattedString(weekFormat, change7d)

                marketCapValue.text = formatter.roundPrice(quote.getMarketCap(), uiItem.currency)
                volumeValue.text = formatter.roundPrice(quote.getVolume24h(), uiItem.currency)

                this.hourChange.text = getFormattedString(
                    R.string.coin_format,
                    getString(R.string.one_hour),
                    oneHourValue
                )
                this.dayChange.text = getFormattedString(
                    R.string.coin_format,
                    getString(R.string.one_day),
                    oneDayValue
                )
                this.weekChange.text =
                    getFormattedString(R.string.coin_format, getString(R.string.week), weekValue)

                val change1hColor =
                    if (change1h >= 0.0f) R.color.material_green700 else R.color.material_red700
                val change24hColor =
                    if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
                val change7dColor =
                    if (change7d >= 0.0f) R.color.material_green700 else R.color.material_red700

                this.hourChange.setTextColor(change1hColor.toColor(context))
                this.dayChange.setTextColor(change24hColor.toColor(context))
                this.weekChange.setTextColor(change7dColor.toColor(context))
            }

            like.tag = item
            like.setLiked(uiItem.favorite)

        }
    }

    internal class QuoteViewHolder(
        formatter: CurrencyFormatter,
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ) : ViewHolder(formatter, view, adapter as CoinAdapter) {

        private val circulatingSupply: View
        private val totalSupply: View
        private val maxSupply: View

        private val circulatingTitle: MaterialTextView
        private val circulatingValue: MaterialTextView
        private val totalTitle: MaterialTextView
        private val totalValue: MaterialTextView
        private val maxTitle: MaterialTextView
        private val maxValue: MaterialTextView

        private val lastUpdated: MaterialTextView

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

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val item = uiItem.item
            val symbol = item.symbol

            val circulating = formatter.roundPrice(item.getCirculatingSupply()) + " " + symbol
            val total = formatter.roundPrice(item.getTotalSupply()) + " " + symbol
            val max = formatter.roundPrice(item.getMaxSupply()) + " " + symbol

            circulatingTitle.setText(R.string.circulating_supply)
            totalTitle.setText(R.string.total_supply)
            maxTitle.setText(R.string.max_supply)

            circulatingValue.text = circulating
            totalValue.text = total
            maxValue.text = max
            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                item.getLastUpdated(),
                TimeUtilKt.currentMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            lastUpdated.text = lastUpdatedTime
        }
    }
}