package com.dreampany.history.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.history.data.model.History
import com.dreampany.history.ui.adapter.HistoryAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryItem private constructor(item: History, @LayoutRes layoutId: Int = 0) :
    BaseItemKt<History, HistoryItem.ViewHolder, String>(item, layoutId) {

    companion object {
        /*fun getItem(item: Word): WordItem {
            return WordItem(item, R.layout.item_word)
        }*/
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.id.startsWith(constraint, true);
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItemKt.ViewHolder(view, adapter) {

         private var adapter: HistoryAdapter
        //private var text: TextView

        init {
            this.adapter = adapter as HistoryAdapter
            //text = view.findViewById(R.id.text_word)
        }

        override fun <VH : BaseItemKt.ViewHolder, T : BaseKt, S : Serializable, I : BaseItemKt<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as HistoryItem

        }
    }
}