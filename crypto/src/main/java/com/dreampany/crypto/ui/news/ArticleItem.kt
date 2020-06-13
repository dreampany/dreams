package com.dreampany.crypto.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.crypto.R
import com.dreampany.crypto.misc.exts.setUrl
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.data.model.Article
import com.dreampany.crypto.data.model.Ticker
import com.dreampany.crypto.databinding.TickerItemBinding
import com.dreampany.crypto.misc.func.CurrencyFormatter
import com.dreampany.framework.misc.exts.context
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticleItem
private constructor(
    val input: Article
) : ModelAbstractBindingItem<Article, TickerItemBinding>(input) {

    companion object {
        fun getItem(
            input: Article
        ): ArticleItem = ArticleItem(input)
    }

    override fun hashCode(): Int = Objects.hashCode(input.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ArticleItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_ticker_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        TickerItemBinding.inflate(inflater, parent, false)


    override fun bindView(bind: TickerItemBinding, payloads: List<Any>) {
        /*bind.rank.text = rank.toString()

        bind.layoutSimple.icon.setUrl(input.market.image)
        bind.layoutSimple.textPair.text =
            bind.context.getString(R.string.format_currency_pair, input.base, input.target)
        bind.layoutSimple.textMarket.text = input.market.name
        bind.layoutPrice.textPrice.text = formatter.format(Currency.USD, input.convertedLast.usd)
        bind.layoutPrice.textVolume.text = bind.context.getString(
            R.string.format_volume_price,
            formatter.format(Currency.USD, input.convertedVolume.usd)
        )*/
    }

    override fun unbindView(binding: TickerItemBinding) {

    }
}