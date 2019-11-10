package com.dreampany.tools.ui.model

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.TextMessage
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.MessageAdapter
import com.dreampany.tools.ui.adapter.WordAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TextMessageItem
private constructor(
    item: TextMessage,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : MessageItem<TextMessageItem.ViewHolder, TextMessage, String>(item, layoutId) {

    companion object {
        fun getItem(item: TextMessage): TextMessageItem {
            return TextMessageItem(item, R.layout.item_text_message_out)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter as MessageAdapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.text.contains(constraint, true)
    }

    class ViewHolder(view: View, adapter: MessageAdapter) : MessageItem.ViewHolder(view, adapter) {

        //private var adapter: WordAdapter
        private var viewWord: TextView
        private var viewPartOfSpeech: TextView
        private var viewPronunciation: TextView

        init {
           // this.adapter = adapter as WordAdapter
            viewWord = view.findViewById(R.id.text_word)
            viewPartOfSpeech = view.findViewById(R.id.text_part_of_speech)
            viewPronunciation = view.findViewById(R.id.text_pronunciation)

/*            view.setOnClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.OPEN
                )
            }*/
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as TextMessageItem
            val item = uiItem.item


        }
    }
}