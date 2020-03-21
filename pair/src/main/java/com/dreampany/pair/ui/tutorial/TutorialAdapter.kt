package com.dreampany.pair.ui.tutorial

import androidx.databinding.ViewDataBinding
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.pair.R
import com.dreampany.pair.databinding.TutorialItemBinding

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TutorialAdapter(listener: Any? = null) :
    BaseAdapter<TutorialItem, TutorialAdapter.ViewHolder>(listener) {

    override fun layoutId(viewType: Int): Int = R.layout.tutorial_item

    override fun createViewHolder(bind: ViewDataBinding, viewType: Int): ViewHolder =
        ViewHolder(bind as TutorialItemBinding)

    inner class ViewHolder(val bind: TutorialItemBinding) :
        BaseAdapter.ViewHolder<TutorialItem, ViewHolder>(bind) {

        override fun bindView(item: TutorialItem, position: Int) {
            bind.text.text = item.title
        }

    }
}