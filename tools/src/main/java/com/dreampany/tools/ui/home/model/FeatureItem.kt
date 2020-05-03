package com.dreampany.tools.ui.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.extension.context
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
    val item: Feature
) : ModelAbstractBindingItem<Feature, FeatureItemBinding>(item) {

    override fun hashCode(): Int = item.hashCode()

    override fun equals(other: Any?): Boolean = item.equals(other)

    override val type: Int
        get() = R.id.adapter_feature_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): FeatureItemBinding =
        FeatureItemBinding.inflate(inflater, parent, false)

    override fun bindView(bind: FeatureItemBinding, payloads: List<Any>) {
        bind.card.setCardBackgroundColor(item.color)
        bind.icon.setImageResource(item.iconRes)
        bind.title.text = bind.context.getText(item.titleRes)
    }

    override fun unbindView(binding: FeatureItemBinding) {

    }
}