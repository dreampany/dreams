package com.dreampany.tools.ui.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.context
import com.dreampany.framework.misc.exts.toTintByColor
import com.dreampany.framework.misc.exts.visible
import com.dreampany.tools.R
import com.dreampany.tools.data.model.home.Feature
import com.dreampany.tools.databinding.FeatureItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FeatureItem(
    val input: Feature
) : ModelAbstractBindingItem<Feature, FeatureItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override val type: Int
        get() = R.id.adapter_feature_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): FeatureItemBinding =
        FeatureItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: FeatureItemBinding, payloads: List<Any>) {
        //bind.card.setCardBackgroundColor(item.color)
        binding.icon.setImageResource(input.iconRes)
        binding.title.text = binding.context.getString(input.titleRes)
        binding.full.visible(input.full)

        binding.icon.toTintByColor(input.color)
        binding.title.setTextColor(input.color)
    }

    override fun unbindView(binding: FeatureItemBinding) {
        binding.full.text = null
        binding.title.text = null
    }
}