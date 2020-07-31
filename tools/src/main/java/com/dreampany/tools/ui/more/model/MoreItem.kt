package com.dreampany.tools.ui.more.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.context
import com.dreampany.framework.misc.exts.dimension
import com.dreampany.framework.misc.exts.screenWidth
import com.dreampany.framework.misc.exts.spanHeight
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
class MoreItem(val input: More) : ModelAbstractBindingItem<More, MoreItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_more_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): MoreItemBinding {
        val height = inflater.context.spanHeight(3, inflater.context.dimension(R.dimen.recycler_spacing).toInt())
        val bind = MoreItemBinding.inflate(inflater, parent, false)
        bind.root.layoutParams.height = height
        return bind
    }

    override fun bindView(bind: MoreItemBinding, payloads: List<Any>) {
        bind.icon.setImageResource(input.iconRes)
        bind.title.text = bind.context.getText(input.titleRes)
    }

    override fun unbindView(binding: MoreItemBinding) {
    }
}