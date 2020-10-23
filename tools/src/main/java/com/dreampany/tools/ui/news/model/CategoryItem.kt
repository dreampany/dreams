package com.dreampany.tools.ui.news.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.toTintByColor
import com.dreampany.tools.data.model.news.Category
import com.dreampany.tools.databinding.CategoryItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.mikepenz.fastadapter.drag.IDraggable
import com.dreampany.tools.R
import com.google.common.base.Objects

/**
 * Created by roman on 15/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CategoryItem(
    val input: Category,
    var favorite: Boolean = false,
    var select: Boolean = false,
    var color: Int = 0
) : ModelAbstractBindingItem<Category, CategoryItemBinding>(input), IDraggable {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as CategoryItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override val isDraggable: Boolean = true

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_category_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): CategoryItemBinding =
        CategoryItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: CategoryItemBinding, payloads: List<Any>) {
        //bind.layout.setCardBackgroundColor(color)
        binding.letter.text = input.title.first().toTitleCase().toString()
        binding.letter.setTextColor(color)
        binding.title.text = input.title
        binding.title.setBackgroundColor(color)
        val selectRes = if (select) R.drawable.ic_baseline_radio_button_checked_24 else R.drawable.ic_baseline_radio_button_unchecked_24
        binding.selection.setImageResource(selectRes)
        binding.selection.toTintByColor(color)
    }

    override fun unbindView(binding: CategoryItemBinding) {
        binding.letter.text = null
        binding.title.text = null
        binding.selection.setImageResource(0)
    }
}