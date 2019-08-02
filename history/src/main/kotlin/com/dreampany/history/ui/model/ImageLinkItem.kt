package com.dreampany.history.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.ui.model.BaseItemKt
import com.dreampany.frame.util.DisplayUtil
import com.dreampany.frame.util.FrescoUtil
import com.dreampany.history.R
import com.dreampany.history.data.model.ImageLink
import com.dreampany.history.ui.adapter.ImageLinkAdapter
import com.facebook.drawee.view.SimpleDraweeView
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
class ImageLinkItem private constructor(
    item: ImageLink, @LayoutRes layoutId: Int = 0
) : BaseItemKt<ImageLink, ImageLinkItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: ImageLink): ImageLinkItem {
            return ImageLinkItem(item, R.layout.item_history_image)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        val link = item as ImageLink
        return link.title.contains(constraint, true)
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItemKt.ViewHolder(view, adapter) {

        private val height: Int

        private var adapter: ImageLinkAdapter
        private var draweeImage: SimpleDraweeView
        private var textTitle: AppCompatTextView

        init {
            this.adapter = adapter as ImageLinkAdapter
            height = DisplayUtil.getScreenWidthInPx(getContext()) / this.adapter.getSpanCount()
            view.layoutParams.height = height
            draweeImage = view.findViewById(R.id.drawee_image)
            textTitle = view.findViewById(R.id.text_title)
        }

        override fun <VH : BaseItemKt.ViewHolder, T : BaseKt, S : Serializable, I : BaseItemKt<T, VH, S>>
                bind(position: Int, item: I) {
            val uiItem = item as ImageLinkItem
            val link = uiItem.item
            FrescoUtil.loadImage(draweeImage, link.id, false)
            Timber.v("link- Title (%s) Url (%s)", link.title, link.id)
            textTitle.text = link.title
        }
    }
}