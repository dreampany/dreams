package com.dreampany.tools.ui.radio.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.common.data.enums.Order
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.databinding.StationItemBinding
import com.dreampany.tools.misc.CurrencyFormatter
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationItem(
    val item: Station,
    val order: StationOrder
    ) : ModelAbstractBindingItem<Station, StationItemBinding>(item) {

    init {

    }

    override fun hashCode(): Int = Objects.hashCode(item.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as StationItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override val type: Int
        get() = R.id.adapter_coin_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): StationItemBinding =
        StationItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: StationItemBinding, payloads: List<Any>) {

    }

    override fun unbindView(binding: StationItemBinding) {

    }
}