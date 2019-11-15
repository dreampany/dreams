package com.dreampany.tools.ui.model

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.FrescoUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.CoinAdapter
import com.dreampany.tools.util.CurrencyFormatter
import com.facebook.drawee.view.SimpleDraweeView
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
    var formatter: CurrencyFormatter,
    var currency: Currency,
    item: Coin,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<CoinItem.ViewHolder, Coin, String>(item, layoutId) {

    companion object {
        fun getItem(formatter: CurrencyFormatter, currency: Currency, item: Coin): CoinItem {
            return CoinItem(formatter, currency, item, R.layout.item_coin)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): CoinItem.ViewHolder {
        return CoinItem.ViewHolder(formatter, view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.id.contains(constraint, true)
    }

    class ViewHolder(val formatter: CurrencyFormatter, view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private var adapter: CoinAdapter
        private var icon: SimpleDraweeView
        private var name: AppCompatTextView
        private var price: AppCompatTextView
        private var hourChange: AppCompatTextView
        private  var dayChange: AppCompatTextView
        private var weekChange: AppCompatTextView
        private var marketCap: AppCompatTextView
        private var dayVolume: AppCompatTextView
        private var lastUpdated: AppCompatTextView

        private  var buttonFavorite: LikeButton
        private  var buttonAlert: LikeButton

        val btcFormat: String
        val positiveChange: Int
        val negativeChange: Int

        init {
            this.adapter = adapter as CoinAdapter

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

            buttonAlert.visibility = View.GONE

            btcFormat = getString(R.string.btc_format)
            positiveChange = R.string.positive_pct_format
            negativeChange = R.string.negative_pct_format
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as CoinItem
            val item = uiItem.item

            val imageUrl = String.format(Locale.ENGLISH, Constants.Api.Crypto.CoinMarketCapImageUrl, item.id)
            FrescoUtil.loadImage(icon, imageUrl, true)

            val nameText = String.format(Locale.ENGLISH, getString(R.string.full_name), item.symbol, item.name)
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
        }
    }
}