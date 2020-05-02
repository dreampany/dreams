package com.dreampany.tools.ui.crypto.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.dreampany.tools.R
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.databinding.ExchangeItemBinding
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExchangeItem
private constructor(
    val item: Exchange,
    val formatter: CurrencyFormatter
) : ModelAbstractBindingItem<Exchange, ExchangeItemBinding>(item) {

    companion object {
        fun getItem(
            item: Exchange,
            formatter: CurrencyFormatter
        ): ExchangeItem = ExchangeItem(item, formatter)
    }

    @StringRes
    private val btcFormat: Int

    @StringRes
    private val positiveRatio: Int

    @StringRes
    private val negativeRatio: Int

    init {
        btcFormat = R.string.btc_format
        positiveRatio = R.string.positive_ratio_format
        negativeRatio = R.string.negative_ratio_format
    }

    override fun hashCode(): Int = Objects.hashCode(item.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ExchangeItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_exchange_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ExchangeItemBinding =
        ExchangeItemBinding.inflate(inflater, parent, false)


    override fun bindView(bind: ExchangeItemBinding, payloads: List<Any>) {

    }

    override fun unbindView(binding: ExchangeItemBinding) {

    }
}