package com.dreampany.tube.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.toTintByColor
import com.dreampany.tube.R
import com.dreampany.tube.data.model.Page
import com.dreampany.tube.databinding.PageItemBinding
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.mikepenz.fastadapter.drag.IDraggable

/**
 * Created by roman on 1/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageItem(
    val input: Page,
    var favorite: Boolean = false,
    var select: Boolean = false,
    var color: Int = 0
) : ModelAbstractBindingItem<Page, PageItemBinding>(input), IDraggable {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as PageItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override val isDraggable: Boolean = true

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_page_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): PageItemBinding =
        PageItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: PageItemBinding, payloads: List<Any>) {
        //bind.layout.setCardBackgroundColor(color)
        bind.letter.text = input.title.firstOrNull()?.toTitleCase().toString()
        bind.letter.setTextColor(color)
        bind.title.text = input.title
        bind.title.setBackgroundColor(color)
        val selectRes = if (select) R.drawable.ic_baseline_radio_button_checked_24 else R.drawable.ic_baseline_radio_button_unchecked_24
        bind.selection.setImageResource(selectRes)
        bind.selection.toTintByColor(color)
    }

    override fun unbindView(bind: PageItemBinding) {
        bind.letter.text = null
        bind.title.text = null
        bind.selection.setImageResource(0)
    }
}