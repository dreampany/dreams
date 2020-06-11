package com.dreampany.crypto.ui.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.data.model.Exchange
import com.dreampany.crypto.data.model.Ticker
import com.dreampany.crypto.databinding.ExchangeItemBinding
import com.dreampany.crypto.databinding.TickerItemBinding
import com.dreampany.crypto.misc.exts.setUrl
import com.dreampany.crypto.misc.func.CurrencyFormatter
import com.dreampany.framework.misc.extension.color
import com.dreampany.framework.misc.extension.context
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TickerItem
private constructor(
    val input: Ticker,
    val formatter: CurrencyFormatter
) : ModelAbstractBindingItem<Ticker, TickerItemBinding>(input) {

    companion object {
        fun getItem(
            input: Ticker,
            formatter: CurrencyFormatter
        ): TickerItem = TickerItem(input, formatter)
    }

    var rank: Int = 0

    override fun hashCode(): Int = Objects.hashCode(input.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as TickerItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_ticker_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        TickerItemBinding.inflate(inflater, parent, false)


    override fun bindView(bind: TickerItemBinding, payloads: List<Any>) {
        bind.rank.text = rank.toString()

        bind.layoutSimple.icon.setUrl(input.market.image)
        bind.layoutSimple.textPair.text =
            bind.context.getString(R.string.format_currency_pair, input.base, input.target)
        bind.layoutSimple.textMarket.text = input.market.name
        bind.layoutPrice.textPrice.text = formatter.format(Currency.USD, input.convertedLast.usd)
        bind.layoutPrice.textVolume.text = bind.context.getString(
            R.string.format_volume_price,
            formatter.format(Currency.USD, input.convertedVolume.usd)
        )
    }

    override fun unbindView(binding: TickerItemBinding) {

    }
}