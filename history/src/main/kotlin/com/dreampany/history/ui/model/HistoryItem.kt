package com.dreampany.history.ui.model

import android.text.util.Linkify
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.frame.util.FrescoUtil
import com.dreampany.frame.util.TextUtil
import com.dreampany.history.R
import com.dreampany.history.data.model.History
import com.dreampany.history.ui.adapter.HistoryAdapter
import com.facebook.drawee.view.SimpleDraweeView
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
        fun getItem(item: History): HistoryItem {
            return HistoryItem(item, R.layout.item_history)
        }
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
        //private var imageIcon: SimpleDraweeView
        //private var textTitle: AppCompatTextView
        //private var textText: AppCompatTextView
        private var textHtml: AppCompatTextView
        private var textYear: AppCompatTextView

        init {
            this.adapter = adapter as HistoryAdapter
            //imageIcon = view.findViewById(R.id.image_icon)
            //textTitle = view.findViewById(R.id.text_title)
            //textText = view.findViewById(R.id.text_text)
            textHtml = view.findViewById(R.id.text_html)
            textYear = view.findViewById(R.id.text_year)
        }

        override fun <VH : BaseItemKt.ViewHolder, T : BaseKt, S : Serializable, I : BaseItemKt<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as HistoryItem
            val history = uiItem.item
           // loadImage(history)
            textHtml.text = HtmlCompat.fromHtml(history.html!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
            textYear.text = TextUtil.getString(getContext(), R.string.year_format, history.year)
            Linkify.addLinks(textHtml, Linkify.WEB_URLS)
        }

        private fun loadImage(history: History) {
            if (history.hasLink()) {
                val link = history.getFirstLink()
                val url = link?.link
                //FrescoUtil.loadImage(imageIcon, url, false)
            }
        }
    }
}