package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.ui.widget.TextDrawable
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.TextUtilKt
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
    item: Feature,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem< FeatureItem.ViewHolder, Feature, String>(item, layoutId) {

    var color: Int = 0
    var order: Int = 0

    init {
        color = ColorUtil.getMaterialRandomColor()
    }

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

/*    fun order(): Int {
        return item.type.ordinal
    }*/

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {

        private val height: Int

        private var adapter: FeatureAdapter
        private var imageIcon: AppCompatImageView
        private var textTitle: AppCompatTextView

        init {
            this.adapter = adapter as FeatureAdapter
            height = getSpanHeight(this.adapter.getSpanCount(), this.adapter.getItemOffset())
            imageIcon = view.findViewById(R.id.image_icon)
            textTitle = view.findViewById(R.id.text_title)

            view.layoutParams.height = height
/*            view.setOnClickListener { view ->
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)
                )
            }*/
            view.setOnSafeClickListener { view ->
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.DEFAULT
                )
            }
            view.setOnLongClickListener { view ->
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.DEFAULT
                )
                true
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            val feature = (item as FeatureItem).item
            val type = feature.type
            val drawable = TextDrawable.builder().buildRound(TextUtilKt.getFirst(type.name), item.color)
            imageIcon.setImageDrawable(drawable)
            textTitle.text = feature.title
        }
    }
}