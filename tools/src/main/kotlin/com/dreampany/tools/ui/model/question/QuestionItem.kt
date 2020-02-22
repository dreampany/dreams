package com.dreampany.tools.ui.model.question

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.ui.widget.TextDrawable
import com.dreampany.framework.util.ColorUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.data.model.word.QuizOption
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.QuizOptionAdapter
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.tools.ui.model.word.QuizOptionItem
import com.google.android.material.textview.MaterialTextView
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuestionItem
private constructor(
    item: Question,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<QuestionItem.ViewHolder, Question, String>(item, layoutId) {

    companion object {
        fun getItem(item: Question): QuestionItem {
            var layoutId = 0
            if (item.type == Question.Type.TRUE_FALSE) {
                layoutId = R.layout.item_question_true_false
            } else if (item.type == Question.Type.MULTIPLE) {
                layoutId = R.layout.item_question_multiple
            }
            return QuestionItem(item, layoutId)
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as QuestionItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        if (item.type == Question.Type.TRUE_FALSE) {
            return TrueFalseViewHolder(view, adapter)
        }
        return MultipleViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        return false
    }


    abstract class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        stickyHeader: Boolean = Constants.Default.BOOLEAN
    ) : BaseItem.ViewHolder(view, adapter, stickyHeader) {

        protected var textTitle: MaterialTextView

        init {
            textTitle = view.findViewById(R.id.text_title)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            val uiItem = item as QuestionItem
            val item = uiItem.item

            textTitle.text = item.question
        }
    }

    internal class TrueFalseViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        QuestionItem.ViewHolder(view, adapter, stickyHeader = true) {


        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
        }
    }

    internal class MultipleViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        QuestionItem.ViewHolder(view, adapter, stickyHeader = true) {


        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
        }
    }
}