package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.ui.widget.TextDrawable
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.DisplayUtil
import com.dreampany.frame.util.TextUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Feature
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.FeatureAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable


/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FeatureItem private constructor(
    item: Feature, @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Feature, FeatureItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: Feature): FeatureItem {
            return FeatureItem(item, R.layout.item_feature)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        return false
    }

    fun order(): Int {
        return item.type.ordinal
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {

        private val height: Int

        private var adapter: FeatureAdapter
        private var imageIcon: AppCompatImageView

        init {
            this.adapter = adapter as FeatureAdapter
            height = getSpanHeight(this.adapter.getSpanCount(), this.adapter.getItemOffset())
            imageIcon = view.findViewById(R.id.image_icon)


            view.layoutParams.height = height
            view.setOnClickListener { view ->
                this.adapter.click?.onClick(item = this.adapter.getItem(adapterPosition))
            }
            view.setOnLongClickListener { view ->
                this.adapter.click?.onClick(item = this.adapter.getItem(adapterPosition))
                true
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            val feature = (item as FeatureItem).item
            val type = feature.type
            val drawable = TextDrawable.builder()
                .buildRound(TextUtilKt.getFirst(type.name), ColorUtil.getMaterialRandomColor())
            imageIcon.setImageDrawable(drawable)
        }
    }
}