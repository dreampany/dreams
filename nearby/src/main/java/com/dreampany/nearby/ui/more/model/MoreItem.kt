package com.dreampany.nearby.ui.more.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.context
import com.dreampany.nearby.R
import com.dreampany.nearby.data.model.more.More
import com.dreampany.nearby.databinding.MoreItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MoreItem(val input: More) : ModelAbstractBindingItem<More, MoreItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override val type: Int
        get() = R.id.adapter_more_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): MoreItemBinding =
        MoreItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: MoreItemBinding, payloads: List<Any>) {
        bind.icon.setImageResource(input.iconRes)
        bind.title.text = bind.context.getText(input.titleRes)
    }

    override fun unbindView(binding: MoreItemBinding) {

    }
}