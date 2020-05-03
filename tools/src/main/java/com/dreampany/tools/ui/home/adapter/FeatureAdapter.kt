package com.dreampany.tools.ui.home.adapter

import androidx.databinding.ViewDataBinding
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.adapter.BaseAdapter
import com.dreampany.tools.R
import com.dreampany.tools.databinding.FeatureItemBinding
import com.dreampany.tools.data.model.home.Feature

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FeatureAdapter(listener: Any? = null) :
    BaseAdapter<Feature, FeatureAdapter.ViewHolder>(listener) {

    override fun layoutId(viewType: Int): Int = R.layout.feature_item

    override fun createViewHolder(bind: ViewDataBinding, viewType: Int): ViewHolder =
        ViewHolder(bind as FeatureItemBinding, this)

    inner class ViewHolder(val bind: FeatureItemBinding, adapter: FeatureAdapter) :
        BaseAdapter.ViewHolder<Feature, ViewHolder>(bind) {

        init {
            bind.root.setOnSafeClickListener {
                adapter.getItem(adapterPosition)?.let {
                    adapter.listener?.onItemClick(it)
                }
            }
        }

        override fun bindView(item: Feature, position: Int) {
            bind.card.setBackgroundColor(item.color)
            bind.icon.setImageResource(item.iconRes)
            bind.title.text = context.getText(item.titleRes)
        }

    }
}