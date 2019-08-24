package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.NoteAdapter
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable


/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteItem
private constructor(
    item: Note,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<Note, NoteItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: Note): NoteItem {
            return NoteItem(item, R.layout.item_note)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as NoteItem
        return Objects.equal(this.item, item.item)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        val note: Note = item
        return note.title.contains(constraint, true)    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {

        private val height: Int

        private var adapter: NoteAdapter
        private var layoutRoot: CardView
        private var textTitle: AppCompatTextView
        private var textDescription: AppCompatTextView
        private var textDate: AppCompatTextView

        init {
            this.adapter = adapter as NoteAdapter
            height = getSpanHeight(this.adapter.getSpanCount(), this.adapter.getItemOffset())

            layoutRoot = view.findViewById(R.id.layout_root)
            textTitle = view.findViewById(R.id.text_title)
            textDescription = view.findViewById(R.id.text_description)
            textDate = view.findViewById(R.id.text_date)

            view.setOnClickListener {
                this.adapter.uiItemClick?.onClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition),
                    action = Action.OPEN
                )
            }
            view.setOnLongClickListener { view ->
                this.adapter.uiItemClick?.onLongClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition),
                    action = Action.OPTIONS
                )
                true
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>> bind(
            position: Int,
            item: I
        ) {
            val uiItem = item as NoteItem
            val item = uiItem.item

            textTitle.text = item.title
            textDescription.text = item.description
            textDate.text = TimeUtilKt.getDate(item.time, Constants.Date.FORMAT_MONTH_DAY)


            val randColor = ColorUtil.getRandCompatColor(getContext())
            layoutRoot.setCardBackgroundColor(randColor)
        }
    }
}