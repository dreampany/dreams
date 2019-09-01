package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.ui.widget.TextDrawable
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.TextUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.QuizAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-08-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RelatedQuizItem
private constructor(
    item: Quiz,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Quiz, RelatedQuizItem.ViewHolder, String>(item, layoutId) {

    var color: Int = 0

    init {
        color = ColorUtil.getMaterialRandomColor()
    }

    companion object {
        fun getItem(item: Quiz): RelatedQuizItem {
            return RelatedQuizItem(item)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): RelatedQuizItem.ViewHolder {
        return RelatedQuizItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private var adapter: QuizAdapter
        private var imageIcon: AppCompatImageView
        private var textTitle: AppCompatTextView

        init {
            this.adapter = adapter as QuizAdapter
            imageIcon = view.findViewById(R.id.image_icon)
            textTitle = view.findViewById(R.id.text_title)

            view.setOnClickListener { view ->
                this.adapter.uiItemClick?.onClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as RelatedQuizItem
            val item = uiItem.item
            val subtype = item.subtype
            val drawable = TextDrawable.builder().buildRound(TextUtilKt.getFirst(item.title), uiItem.color)
            imageIcon.setImageDrawable(drawable)
            textTitle.text = item.title
        }
    }
}