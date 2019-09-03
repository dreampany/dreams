package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Color
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.ui.widget.TextDrawable
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.TextUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.model.QuizOption
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.QuizOptionAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-09-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuizOptionItem
private constructor(
    item: QuizOption,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<QuizOption, QuizOptionItem.ViewHolder, String>(item, layoutId) {

    var color: Color

    init {
        color = ColorUtil.createGreyColor()
    }

    companion object {
        fun getItem(item: QuizOption): QuizOptionItem {
            return QuizOptionItem(item, R.layout.item_quiz_option)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): QuizOptionItem.ViewHolder {
        return QuizOptionItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private var adapter: QuizOptionAdapter
        private var imageIcon: AppCompatImageView
        private var textTitle: AppCompatTextView
        private var imageStatus: AppCompatImageView

        init {
            this.adapter = adapter as QuizOptionAdapter
            imageIcon = view.findViewById(R.id.image_icon)
            textTitle = view.findViewById(R.id.text_title)
            imageStatus = view.findViewById(R.id.image_status)

            view.setOnClickListener { view ->
                this.adapter.uiItemClick?.onClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as QuizOptionItem
            val item = uiItem.item
           // val drawable = TextDrawable.builder().buildRound(TextUtilKt.getFirst(item.title), uiItem.color)
           // imageIcon.setImageDrawable(drawable)
            //textTitle.text = item.title
        }
    }
}