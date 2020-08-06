package com.dreampany.tube.ui.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.tube.R
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.databinding.CategoryItemBinding
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.mikepenz.fastadapter.drag.IDraggable

/**
 * Created by roman on 1/7/20
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

    override fun bindView(bind: CategoryItemBinding, payloads: List<Any>) {
        bind.layout.setCardBackgroundColor(color)
        bind.title.text = input.title
        val selectRes = if (select) R.drawable.ic_baseline_radio_button_checked_24 else R.drawable.ic_baseline_radio_button_unchecked_24

        bind.selection.setImageResource(selectRes)
    }

    override fun unbindView(binding: CategoryItemBinding) {

    }

    val isPureSelected : Boolean
        get() = select || input.type.isFixed
}