package com.dreampany.tools.ui.crypto.adapter

import androidx.databinding.ViewDataBinding
import com.dreampany.common.misc.extension.setOnSafeClickListener
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.tools.R
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.databinding.CoinItemBinding

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinAdapter(listener: Any? = null) :
    BaseAdapter<Coin, CoinAdapter.ViewHolder>(listener) {

    override fun layoutId(viewType: Int): Int = R.layout.feature_item

    override fun createViewHolder(bind: ViewDataBinding, viewType: Int): ViewHolder =
        ViewHolder(bind as CoinItemBinding, this)

    inner class ViewHolder(val bind: CoinItemBinding, adapter: CoinAdapter) :
        BaseAdapter.ViewHolder<Coin, ViewHolder>(bind) {

        init {
            bind.root.setOnSafeClickListener {
                adapter.getItem(adapterPosition)?.let {
                    adapter.listener?.onItemClick(it)
                }
            }
        }

        override fun bindView(item: Coin, position: Int) {
        }

    }
}