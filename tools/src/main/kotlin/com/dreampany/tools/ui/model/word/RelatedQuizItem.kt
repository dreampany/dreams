package com.dreampany.tools.ui.model.word

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Color
import com.dreampany.framework.data.model.Point
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.TextUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.word.QuizOption
import com.dreampany.tools.data.model.word.RelatedQuiz
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
) : BaseItem<RelatedQuizItem.ViewHolder, RelatedQuiz, String>(item, layoutId) {

    private var color: Color
    private var optionItems: ArrayList<QuizOptionItem>? = Constants.Default.NULL
    var point: Point? = Constants.Default.NULL
    var typePoint: Point? = Constants.Default.NULL
    var totalPoint: Point? = Constants.Default.NULL
    var typeCount: Int = Constants.Default.INT
    var totalCount: Int = Constants.Default.INT

    init {
        color = ColorUtil.createGreyColor()
    }

    companion object {
        fun getItem(item: RelatedQuiz): RelatedQuizItem {
            return RelatedQuizItem(item)
        }
    }

    fun played(): Boolean {
        if (point == null) return false
        return point?.points != 0L
    }

    fun isWinner(): Boolean {
        if (point == null) return false
        return point!!.points > 0
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(
            view,
            adapter
        )
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    fun getOptionItems(context: Context): List<QuizOptionItem> {
        if (optionItems.isNullOrEmpty()) {
            optionItems = ArrayList()
            //optionItems!!.add(getHeaderOptionItem(context))
            for (index in 0..item.options!!.size - 1) {
                optionItems!!.add(
                    getOptionItem(
                        item.options!!.get(index),
                        Constants.Keys.Quiz.OptionCharArray.get(index)
                    )
                )
            }
        }
        item.given?.run {
            val wrong = if (item.answer!!.equals(this)) false else true
            optionItems?.forEach { option ->
                if (option.item.id.equals(item.answer)) {
                    option.state = State.RIGHT
                } else if (wrong && option.item.id.equals(item.given)) {
                    option.state = State.WRONG
                }
            }
        }
        return optionItems!!
    }

    fun getHeaderOptionItem(context: Context): QuizOptionItem {
        val id = TextUtil.getString(
            context,
            R.string.title_quiz_header,
            item.subtype.name.toLowerCase(),
            item.id
        )
        val credit = typePoint!!.points
        val totalCredit = totalPoint!!.points
        val header =
            QuizOption(
                id = id!!,
                header = true
            )
        val item =
            QuizOptionItem.getItem(header)
        item.credit = credit
        item.totalCredit = totalCredit
        item.count = typeCount
        item.totalCount = totalCount
        return item
    }

    private fun getOptionItem(option: String, letter: Char): QuizOptionItem {
        val item = QuizOption(
            id = option,
            letter = letter
        )
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

            view.setOnSafeClickListener { view ->
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.DEFAULT
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
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