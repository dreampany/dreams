package com.dreampany.tools.ui.crypto.adapter

import androidx.databinding.ViewDataBinding
import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.extension.setOnSafeClickListener
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.tools.R
import com.dreampany.tools.api.crypto.misc.CryptoConstants
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.databinding.CoinItemBinding
import com.dreampany.tools.misc.extension.setUrl
import java.util.*

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinAdapter(listener: Any? = null) :
    BaseAdapter<Coin, CoinAdapter.ViewHolder>(listener) {

    private lateinit var currency: Currency
    private lateinit var sort: CoinSort
    private lateinit var order: Order

    override fun layoutId(viewType: Int): Int = R.layout.coin_item

    override fun createViewHolder(bind: ViewDataBinding, viewType: Int): ViewHolder =
        ViewHolder(bind as CoinItemBinding, this)


    fun setProperty(currency: Currency, sort: CoinSort, order: Order, reset: Boolean = false) {
        this.currency = currency
        this.sort = sort
        this.order = order
        if (reset) {
            clearAll()
        }
    }

    inner class ViewHolder(private val bind: CoinItemBinding, private val adapter: CoinAdapter) :
        BaseAdapter.ViewHolder<Coin, ViewHolder>(bind) {

        init {
            bind.root.setOnSafeClickListener {
                adapter.getItem(adapterPosition)?.let {
                    adapter.listener?.onItemClick(it)
                }
            }
        }

        override fun bindView(item: Coin, position: Int) {
            bind.layoutSimple.imageIcon.setUrl(String.format(Locale.ENGLISH, CryptoConstants.CoinMarketCap.IMAGE_URL, item.id))

            bind.layoutSimple.apply {
                val nameText =
                    String.format(
                        Locale.ENGLISH,
                        context.getString(R.string.crypto_symbol_name),
                        item.symbol,
                        item.name
                    )
                textName.text = nameText

                 val quote = item.getQuote(currency)
            }
        }

    }
}