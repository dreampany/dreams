package com.dreampany.tools.ui.model.question

import android.view.View
import android.widget.RadioGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extension.*
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.FeatureAdapter
import com.dreampany.tools.ui.adapter.question.QuestionAdapter
import com.google.android.material.radiobutton.MaterialRadioButton
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

    internal var answer: String? = null
    internal var points: Long = 0L

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


    fun calculatePoints() {
        if (answer.isNullOrEmpty()) {
            return
        }
        if (answer.equals(item.answer)) {
            points = 10
        }
    }

    abstract class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        stickyHeader: Boolean = Constants.Default.BOOLEAN
    ) : BaseItem.ViewHolder(view, adapter, stickyHeader) {

        protected var adapter: QuestionAdapter

        protected var textTitle: MaterialTextView
        protected var radio: RadioGroup

        init {
            this.adapter = adapter as QuestionAdapter

            textTitle = view.findViewById(R.id.text_title)
            radio = view.findViewById(R.id.radio)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            val uiItem = item as QuestionItem
            val item = uiItem.item

            textTitle.text = item.question.toHtml()
        }
    }

    internal class TrueFalseViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        QuestionItem.ViewHolder(view, adapter) {

        protected var button1st: MaterialRadioButton
        protected var button2nd: MaterialRadioButton

        init {
            button1st = view.findViewById(R.id.button_1st)
            button2nd = view.findViewById(R.id.button_2nd)

            radio.setOnCheckedChangeListener { group, checkedId ->
                val item =
                    this.adapter.getItem(adapterPosition) ?: return@setOnCheckedChangeListener
                item.answer = group.findViewById<MaterialRadioButton>(checkedId).text.toString()
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = radio,
                    item = item,
                    action = Action.SOLVE
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            val uiItem = item as QuestionItem
            val item = uiItem.item
            radio.isEnabled = item.answer.isNullOrEmpty()

            button1st.text = item.options?.first()
            button2nd.text = item.options?.last()
        }
    }

    internal class MultipleViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        QuestionItem.ViewHolder(view, adapter) {

        protected var button1st: MaterialRadioButton
        protected var button2nd: MaterialRadioButton
        protected var button3rd: MaterialRadioButton
        protected var button4th: MaterialRadioButton

        init {
            button1st = view.findViewById(R.id.button_1st)
            button2nd = view.findViewById(R.id.button_2nd)
            button3rd = view.findViewById(R.id.button_3rd)
            button4th = view.findViewById(R.id.button_4th)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            val uiItem = item as QuestionItem
            val item = uiItem.item

            radio.isEnabled = item.answer.isNullOrEmpty()

            val first = item.options?.first()
            val second = item.options?.secondOrNull()
            val third = item.options?.thirdOrNull()
            val fourth = item.options?.fourthOrNull()

            button1st.text = first
            button2nd.text = second
            if (third.isNullOrEmpty()) {
                button3rd.invisible()
            } else {
                button3rd.text = third
                button3rd.visible()
            }
            if (fourth.isNullOrEmpty()) {
                button4th.invisible()
            } else {
                button4th.text = fourth
                button4th.visible()
            }

        }
    }
}