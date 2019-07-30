package com.dreampany.history.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.data.model.ImageLink
import com.dreampany.frame.data.model.Link
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.frame.ui.view.TextViewClickMovement
import com.dreampany.frame.util.TextUtil
import com.dreampany.history.R
import com.dreampany.history.data.model.History
import com.dreampany.history.ui.adapter.HistoryAdapter
import com.like.LikeButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import timber.log.Timber
import java.io.Serializable

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryItem private constructor(
    item: History, @LayoutRes layoutId: Int = 0,
    var clickListener: OnClickListener? = null
) : BaseItemKt<History, HistoryItem.ViewHolder, String>(item, layoutId) {

    interface OnClickListener {
        fun onFavoriteClicked(history: History)
        fun onLinkClicked(link: String)
    }

    private var imageBucket: MutableMap<Link, MutableList<ImageLink>>? = null

    companion object {
        fun getItem(item: History, clickListener: OnClickListener? = null): HistoryItem {
            return HistoryItem(item, R.layout.item_history, clickListener)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter, clickListener)
    }

    override fun filter(constraint: String): Boolean {
        val history = item as History
        return history.text!!.contains(constraint, true)
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        var clickListener: OnClickListener? = null
    ) :
        BaseItemKt.ViewHolder(view, adapter),
        TextViewClickMovement.OnTextViewClickMovementListener {

        private var adapter: HistoryAdapter
        //private var imageIcon: SimpleDraweeView
        //private var textTitle: AppCompatTextView
        //private var textText: AppCompatTextView
        private var textHtml: AppCompatTextView
        private var textYear: AppCompatTextView
        private var buttonFavorite: LikeButton

        init {
            this.adapter = adapter as HistoryAdapter
            //imageIcon = view.findViewById(R.id.image_icon)
            //textTitle = view.findViewById(R.id.text_title)
            //textText = view.findViewById(R.id.text_text)
            textHtml = view.findViewById(R.id.text_html)
            textYear = view.findViewById(R.id.text_year)
            buttonFavorite = view.findViewById(R.id.button_favorite)

            textHtml.movementMethod = TextViewClickMovement(this, getContext())
            buttonFavorite.setOnClickListener {
                val uiItem = adapter.getItem(adapterPosition)
                uiItem?.run {
                    clickListener?.onFavoriteClicked(this.item)
                }
            }
        }

        override fun <VH : BaseItemKt.ViewHolder, T : BaseKt, S : Serializable, I : BaseItemKt<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as HistoryItem
            val history = uiItem.item
            // loadImage(history)
            textHtml.text = HtmlCompat.fromHtml(history.html!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textYear.text = TextUtil.getString(getContext(), R.string.year_format, history.year)

            buttonFavorite.isLiked = uiItem.favorite
            setTag(history)
        }

        override fun onLinkClicked(linkText: String, linkType: TextViewClickMovement.LinkType) {

            Timber.v("onLinkClicked- %s ", linkText)
            if (linkText.isNotEmpty()) {
                val history = getTag<History>()
                val link = history?.getLinkByTitle(linkText)
                link?.run {
                    clickListener?.onLinkClicked(this.url)
                }
            }

        }

        override fun onLongClick(text: String) {
        }

        private fun loadImage(history: History) {
            if (history.hasLink()) {
                val link = history.getFirstLink()
                val url = link?.url
                //FrescoUtil.loadImage(imageIcon, url, false)
            }
        }
    }
}