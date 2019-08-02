package com.dreampany.lca.ui.model

import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.frame.util.DisplayUtil
import com.dreampany.frame.util.FrescoUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.lca.R
import com.dreampany.lca.data.model.News
import com.dreampany.lca.ui.adapter.NewsAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import timber.log.Timber
import java.io.Serializable

/**
 * Created by roman on 2019-08-02
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NewsItem private constructor(
    item: News, @LayoutRes layoutId: Int = 0
) : BaseItem<News, NewsItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: News): NewsItem {
            return NewsItem(item, R.layout.item_news)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.title!!.contains(constraint, true)
    }


    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {


        private var adapter: NewsAdapter
        var icon: SimpleDraweeView
        var title: TextView
        var body: TextView
        var source: TextView
        var time: TextView

        init {
            this.adapter = adapter as NewsAdapter
            icon = view.findViewById(R.id.image_icon)
            title = view.findViewById(R.id.text_title)
            body = view.findViewById(R.id.text_body)
            source = view.findViewById(R.id.text_source)
            time = view.findViewById(R.id.text_time)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as NewsItem
            val news = uiItem.item
            FrescoUtil.loadImage(icon, news.imageUrl, true)
            title.text = news.title
            body.text = news.body
            source.text = news.source
            val publishTimeString = DateUtils.getRelativeTimeSpanString(
                news.publishedOn * 1000,
                TimeUtilKt.currentMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            time.text = publishTimeString
        }
    }
}