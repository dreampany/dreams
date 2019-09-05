package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Color
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.ui.widget.TextDrawable
import com.dreampany.framework.util.ColorUtil
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
    @LayoutRes layoutId: Int = Constants.Default.INT,
    state : State = State.DEFAULT
) : BaseItem<QuizOption, QuizOptionItem.ViewHolder, String>(item, layoutId) {

    var color: Color

    init {
        color = ColorUtil.createGreyColor()
    }

    companion object {
        fun getItem(item: QuizOption): QuizOptionItem {
            if (item.header) return QuizOptionItem(item, R.layout.item_quiz_option_header)
            return QuizOptionItem(item, R.layout.item_quiz_option)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        if (item.header) return HeaderViewHolder(view, adapter)
        return ItemViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    abstract class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        stickyHeader: Boolean = Constants.Default.BOOLEAN
    ) : BaseItem.ViewHolder(view, adapter, stickyHeader) {

        protected var adapter: QuizOptionAdapter
        protected lateinit var uiItem: QuizOptionItem
        protected lateinit var item: QuizOption

        init {
            this.adapter = adapter as QuizOptionAdapter
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            uiItem = item as QuizOptionItem
            this.item = uiItem.item
        }

        fun drawLetter(image: AppCompatImageView, text: Char) {
            val drawable = TextDrawable.builder().buildRound(
                text.toString(),
                ColorUtil.getColor(image.context, uiItem.color.primaryId)
            )
            image.setImageDrawable(drawable)
        }
    }


    class HeaderViewHolder(view: View, adapter: FlexibleAdapter<*>) : ViewHolder(view, adapter, stickyHeader = true) {

        private var textTitle: AppCompatTextView

        init {
            textTitle = view.findViewById(R.id.text_title)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            textTitle.text = this.item.id
        }
    }

    class ItemViewHolder(view: View, adapter: FlexibleAdapter<*>) : ViewHolder(view, adapter) {

        private var imageIcon: AppCompatImageView
        private var textTitle: AppCompatTextView
        private var imageStatus: AppCompatImageView

        init {
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

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            drawLetter(imageIcon, this.item.letter)
            textTitle.text = this.item.id
            imageStatus.setImageResource(this.item.stausRes)
        }
    }
}