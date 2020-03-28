package com.dreampany.adapter.holder

import android.view.View
import com.dreampany.adapter.FlexibleAdapter

/**
 * Created by roman on 29/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class FlexibleViewHolder
    : ContentViewHolder, View.OnClickListener {

    protected val adapter: FlexibleAdapter

    constructor(view: View, adapter: FlexibleAdapter) : this(view, adapter, false) {

    }

    constructor(view: View, adapter: FlexibleAdapter, stickyHeader: Boolean) : super(view, adapter, stickyHeader) {
        this.adapter = adapter

//        if (adapter.cickListener != null) {
//
//        }
    }
}