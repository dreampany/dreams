package com.dreampany.crypto.ui.news

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.dreampany.crypto.R
import com.dreampany.crypto.misc.exts.setUrl
import com.dreampany.framework.misc.exts.inflate
import com.facebook.drawee.view.SimpleDraweeView
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by roman on 13/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SliderAdapter(val context: Context) : SliderViewAdapter<SliderAdapter.ViewHolder>() {

    private val items: MutableList<ArticleItem>

    init {
        items = Collections.synchronizedList(ArrayList())
    }

    override fun getCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.news_slider_item))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.icon.setUrl(item.input.imageUrl)
    }

    val isEmpty: Boolean get() = items.isEmpty()

    fun addItems(items: List<ArticleItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : SliderViewAdapter.ViewHolder(view) {

        internal val icon: SimpleDraweeView

        init {
            icon = view.findViewById(R.id.icon)
        }

    }
}