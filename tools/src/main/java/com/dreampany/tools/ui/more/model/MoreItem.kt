package com.dreampany.tools.ui.more.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.common.misc.extension.context
import com.dreampany.tools.R
import com.dreampany.tools.data.model.more.More
import com.dreampany.tools.databinding.MoreItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MoreItem(
    val item: More
    ) : ModelAbstractBindingItem<More, MoreItemBinding>(item) {

    override fun hashCode(): Int = item.hashCode()

    override fun equals(other: Any?): Boolean = item.equals(other)

    override val type: Int
        get() = R.id.adapter_more_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): MoreItemBinding =
        MoreItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: MoreItemBinding, payloads: List<Any>) {
        bind.icon.setImageResource(item.iconRes)
        bind.title.text = bind.context.getText(item.titleRes)
    }

    override fun unbindView(binding: MoreItemBinding) {

    }
}