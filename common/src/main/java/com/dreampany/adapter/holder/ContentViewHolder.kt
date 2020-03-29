package com.dreampany.adapter.holder

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.FlexibleAdapter
import com.dreampany.adapter.item.IFlexible

/**
 * Created by roman on 29/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class ContentViewHolder : RecyclerView.ViewHolder {

    private var backupPosition = RecyclerView.NO_POSITION
    private lateinit var contentView: View

    constructor(view: View, adapter: FlexibleAdapter<FlexibleViewHolder, IFlexible<FlexibleViewHolder>>, stickyHeader: Boolean)
            : super(if (stickyHeader) FrameLayout(view.context) else view) {

        if (stickyHeader) {

        }
    }

    fun setBackupPosition(backupPosition: Int) {
        this.backupPosition = backupPosition
    }

    fun getContentView(): View = if (::contentView.isInitialized) contentView else itemView

    fun getFlexiblePosition(): Int {
        var position = adapterPosition
        if (position == RecyclerView.NO_POSITION) {
            position = backupPosition
        }
        return position
    }

}