package com.dreampany.map.ui.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.dreampany.map.R
import com.dreampany.map.data.model.GooglePlace
import com.dreampany.map.manager.PlaceManager
import com.rishabhharit.roundedimageview.RoundedImageView

/**
 * Created by roman on 2019-12-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PlaceAdapter(listener: Any? = null) : BaseAdapter<GooglePlace, PlaceAdapter.ViewHolder>() {

    override fun getViewType(item: GooglePlace): Int {
        return 0
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_place
    }

    override fun createViewHolder(view: View, viewType: Int): ViewHolder {
        return ViewHolder(view, this)
    }

    inner class ViewHolder(itemView: View, adapter: PlaceAdapter) :
        BaseAdapter.ViewHolder<GooglePlace, ViewHolder>(itemView) {
        private val adapter: PlaceAdapter
        private val parent: View
        private val icon: RoundedImageView
        private val title: AppCompatTextView

        init {
            this.adapter = adapter
            parent = itemView.findViewById(R.id.layout_parent)
            icon = itemView.findViewById(R.id.icon)
            title = itemView.findViewById(R.id.title)
        }

        override fun bindView(holder: ViewHolder, item: GooglePlace) {
            val bitmap = PlaceManager.getBitmap(item.placeId)
            icon.setImageBitmap(bitmap)
            title.text = item.name
        }
    }
}