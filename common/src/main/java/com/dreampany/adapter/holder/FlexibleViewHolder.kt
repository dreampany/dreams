package com.dreampany.adapter.holder

import android.view.View
import com.dreampany.adapter.FlexibleAdapter
import com.dreampany.adapter.item.IFlexible

/**
 * Created by roman on 29/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class FlexibleViewHolder : ContentViewHolder, View.OnClickListener {

    protected val adapter: FlexibleAdapter<FlexibleViewHolder, IFlexible<FlexibleViewHolder>>

    constructor(view: View, adapter: FlexibleAdapter<FlexibleViewHolder, IFlexible<FlexibleViewHolder>>) : this(view, adapter, false) {

    }

    constructor(view: View, adapter: FlexibleAdapter<FlexibleViewHolder, IFlexible<FlexibleViewHolder>>, stickyHeader: Boolean) : super(view, adapter, stickyHeader) {
        this.adapter = adapter

//        if (adapter.cickListener != null) {
//
//        }
    }

    open fun getActivationElevation(): Float = 0f

    open fun toggleActivation() {
        val position = getFlexiblePosition()

    }
}