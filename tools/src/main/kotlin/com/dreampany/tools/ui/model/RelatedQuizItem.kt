package com.dreampany.tools.ui.model

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Color
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.TextUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.QuizOption
import com.dreampany.tools.data.model.RelatedQuiz
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
    item: RelatedQuiz,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<RelatedQuiz, RelatedQuizItem.ViewHolder, String>(item, layoutId) {

    var color: Color

    init {
        color = ColorUtil.createGreyColor()
    }

    companion object {
        fun getItem(item: RelatedQuiz): RelatedQuizItem {
            return RelatedQuizItem(item)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    fun getOptionItems(context: Context): List<QuizOptionItem> {
        val result = ArrayList<QuizOptionItem>()
        result.add(getHeaderOptionItem(context))
        for (index in 0..item.options!!.size-1) {
            result.add(getOptionItem(item.options!!.get(index), Constants.Quiz.OptionCharArray.get(index)))
        }
        return result
    }

    fun getHeaderOptionItem(context: Context): QuizOptionItem {
        val id = TextUtil.getString(
            context,
            R.string.title_quiz_header,
            TextUtil.toTitleCase(item.subtype.name),
            item.id
        )
        val header = QuizOption(id = id!!, header = true)
        return QuizOptionItem.getItem(header)
    }

    fun getOptionItem(option: String, letter: Char): QuizOptionItem {
        val item = QuizOption(id = option, letter = letter)
        return QuizOptionItem.getItem(item)
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
            /*          val drawable = TextDrawable.builder().buildRound(TextUtilKt.getFirst(item.title), uiItem.color)
                      imageIcon.setImageDrawable(drawable)
                      textTitle.text = item.title*/
        }
    }
}