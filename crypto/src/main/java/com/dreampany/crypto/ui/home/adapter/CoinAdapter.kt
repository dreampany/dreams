package com.dreampany.crypto.ui.home.adapter

import android.text.format.DateUtils
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import com.dreampany.crypto.R
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.data.enums.Sort
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.databinding.CoinItemBinding
import com.dreampany.crypto.misc.func.CurrencyFormatter
import com.dreampany.framework.data.enums.Order
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.util.Util
import com.dreampany.framework.ui.adapter.BaseAdapter
import com.dreampany.framework.ui.adapter.SearchAdapter
import java.util.*

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinAdapter(listener: Any? = null) : SearchAdapter<Coin, CoinAdapter.ViewHolder>(listener) {

    private lateinit var formatter: CurrencyFormatter
    private lateinit var currency: Currency
    private lateinit var sort: Sort
    private lateinit var order: Order

    override fun layoutId(viewType: Int): Int = R.layout.coin_item

    override fun createViewHolder(bind: ViewDataBinding, viewType: Int): ViewHolder =
        ViewHolder(bind as CoinItemBinding, this)

    override fun filter(item: Coin, constraint: CharSequence): Boolean =
        item.name.value().contains(constraint)

    fun setProperty(
        currency: Currency,
        sort: Sort,
        order: Order,
        formatter: CurrencyFormatter,
        reset: Boolean = false
    ) {
        this.formatter = formatter
        this.currency = currency
        this.sort = sort
        this.order = order
        if (reset) {
            clearAll()
        }
    }

    inner class ViewHolder(private val bind: CoinItemBinding, private val adapter: CoinAdapter) :
        BaseAdapter.ViewHolder<Coin, ViewHolder>(bind) {

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

            bind.root.setOnSafeClickListener {
                adapter.getItem(adapterPosition)?.let {
                    adapter.listener?.onItemClick(it)
                }
            }
        }

        override fun bindView(item: Coin, position: Int) {
            /*bind.layoutSimple.icon.setUrl(
                String.format(
                    Locale.ENGLISH,
                    ApiConstants.CoinMarketCap.IMAGE_URL,
                    item.id
                )
            )*/


            val nameText =
                String.format(
                    Locale.ENGLISH,
                    context.getString(R.string.crypto_symbol_name),
                    item.symbol,
                    item.name
                )
            bind.layoutSimple.textName.text = nameText

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

            bind.layoutSimple.textPrice.text = formatter.formatPrice(price, currency)
            bind.layoutPrice.textMarketCap.text = formatter.roundPrice(marketCap, currency)
            bind.layoutPrice.textVolume24h.text = formatter.roundPrice(volume24h, currency)

            //val change1hFormat = if (change1h >= 0.0f) positiveRatio else negativeRatio
            val change24hFormat = if (change24h >= 0.0f) positiveRatio else negativeRatio
            //val change7dFormat = if (change7d >= 0.0f) positiveRatio else negativeRatio

            //bind.layoutPrice.textChange1h.text = context.formatString(change1hFormat, change1h)
            bind.layoutPrice.textChange24h.text = context.formatString(change24hFormat, change24h)
            //bind.layoutPrice.textChange7d.text = context.formatString(change7dFormat, change7d)

            val startColor = R.color.material_grey400
            val endColor =
                if (change1h >= 0.0f || change24h >= 0.0f || change7d >= 0.0f) R.color.material_green700 else R.color.material_red700

            bind.layoutSimple.textPrice.blink(startColor, endColor)

            val hourChangeColor =
                if (change1h >= 0.0f) R.color.material_green700 else R.color.material_red700
            //bind.layoutPrice.textChange1h.setTextColor(context.color(hourChangeColor))

            val dayChangeColor =
                if (change24h >= 0.0f) R.color.material_green700 else R.color.material_red700
            bind.layoutPrice.textChange24h.setTextColor(context.color(dayChangeColor))

            val weekChangeColor =
                if (change7d >= 0.0f) R.color.material_green700 else R.color.material_red700
            //bind.layoutPrice.textChange7d.setTextColor(context.color(weekChangeColor))

            val lastUpdatedTime = DateUtils.getRelativeTimeSpanString(
                item.getLastUpdated(),
                Util.currentMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            //bind.layoutSimple.textLastUpdated.text = lastUpdatedTime

        }

    }
}