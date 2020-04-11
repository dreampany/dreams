package com.dreampany.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.FlexibleAdapter
import com.dreampany.adapter.item.IFlexible

/**
 * Created by roman on 29/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class FlexibleViewHolder<VH : RecyclerView.ViewHolder, T : IFlexible<VH>>
    (view: View, protected val adapter: FlexibleAdapter<VH, T>) :
    ContentViewHolder<VH, T>(view, adapter),
    View.OnClickListener, View.OnLongClickListener {

    private var longClickSkipped = false
    private var alreadySelected = false

    init {
        if (adapter.clickListener != null) {
            contentView?.setOnClickListener(this)
        }
        if (adapter.longClickListener != null) {
            contentView?.setOnLongClickListener(this)
        }
    }

    override fun onClick(view: View) {
        val position = flexiblePosition
    }

    override fun onLongClick(view: View): Boolean {
        TODO("Not yet implemented")
    }

    /*open fun getActivationElevation(): Float = 0f

    open fun toggleActivation() {
        val position = getFlexiblePosition()

    }*/
}