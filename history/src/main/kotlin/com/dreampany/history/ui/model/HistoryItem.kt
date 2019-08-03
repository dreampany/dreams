package com.dreampany.history.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Link
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.ui.view.TextViewClickMovement
import com.dreampany.frame.util.TextUtil
import com.dreampany.history.R
import com.dreampany.history.data.model.History
import com.dreampany.history.misc.Constants
import com.dreampany.history.ui.adapter.HistoryAdapter
import com.google.common.collect.Maps
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
class HistoryItem
private constructor(
    item: History, @LayoutRes layoutId: Int = Constants.Default.INT,
    var clickListener: OnClickListener? = null
) : BaseItem<History, HistoryItem.ViewHolder, String>(item, layoutId) {

    interface OnClickListener {
        fun onFavoriteClicked(history: History)
        fun onLinkClicked(link: String)
    }

    private var imageBucket: MutableMap<Link, List<ImageLinkItem>>? = null

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
        val history: History = item
        return history.text!!.contains(constraint, true)
    }

    fun hasBucket(link: Link): Boolean {
        if (imageBucket == null) {
            return false
        }
        if (!imageBucket!!.containsKey(link)) {
            return false
        }
        if (imageBucket!!.get(link).isNullOrEmpty()) {
            return false
        }
        return true
    }

    fun putBucket(link: Link, uiLinkItems: List<ImageLinkItem>) {
        if (imageBucket == null) {
            imageBucket = Maps.newConcurrentMap()
        }
        imageBucket!!.put(link, uiLinkItems)
    }

    fun getImageLinkItems(): List<ImageLinkItem>? {
        if (imageBucket.isNullOrEmpty()) {
            return null
        }
        val links = mutableListOf<ImageLinkItem>()
        imageBucket!!.forEach { entry ->
            links.addAll(entry.value)
        }
        return links
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        var clickListener: OnClickListener? = null
    ) : BaseItem.ViewHolder(view, adapter),
        TextViewClickMovement.OnTextViewClickMovementListener {

        private var adapter: HistoryAdapter
        private var textHtml: AppCompatTextView
        private var textYear: AppCompatTextView
        private var buttonFavorite: LikeButton

        init {
            this.adapter = adapter as HistoryAdapter
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

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as HistoryItem
            val history = uiItem.item
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
                    clickListener?.onLinkClicked(this.id)
                }
            }

        }

        override fun onLongClick(text: String) {
        }
    }
}