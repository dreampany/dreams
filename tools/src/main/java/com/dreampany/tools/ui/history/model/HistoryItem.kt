package com.dreampany.tools.ui.history.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.context
import com.dreampany.framework.misc.exts.html
import com.dreampany.tools.R
import com.dreampany.tools.data.model.history.History
import com.dreampany.tools.databinding.HistoryItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryItem(
    val input: History
) : ModelAbstractBindingItem<History, HistoryItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override val type: Int get() = R.id.adapter_history_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): HistoryItemBinding =
        HistoryItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: HistoryItemBinding, payloads: List<Any>) {
        bind.textHtml.text = input.html?.html
        bind.textYear.text = bind.context.getString(R.string.year_format, input.year)
    }

    override fun unbindView(binding: HistoryItemBinding) {

    }
}