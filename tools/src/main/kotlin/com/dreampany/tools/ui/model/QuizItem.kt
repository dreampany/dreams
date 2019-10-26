package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.ui.widget.TextDrawable
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.TextUtilKt
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
class QuizItem
private constructor(
    item: Quiz,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Quiz, QuizItem.ViewHolder, String>(item, layoutId) {

    var color: Int = 0

    init {
        color = ColorUtil.getMaterialRandomColor()
    }

    companion object {
        fun getItem(item: Quiz): QuizItem {
            return QuizItem(item, R.layout.item_quiz)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): QuizItem.ViewHolder {
        return QuizItem.ViewHolder(view, adapter)
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
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as QuizItem
            val item = uiItem.item
            val subtype = item.subtype
            val drawable = TextDrawable.builder().buildRound(TextUtilKt.getFirst(item.title), uiItem.color)
            imageIcon.setImageDrawable(drawable)
            textTitle.text = item.title
        }
    }
}