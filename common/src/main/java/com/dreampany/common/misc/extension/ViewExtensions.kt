package com.dreampany.common.misc.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.common.misc.func.SafeClickListener

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun ViewGroup.inflater(): LayoutInflater {
    return LayoutInflater.from(context)
}

fun Int.inflater(parent: ViewGroup, attachToRoot: Boolean = false): View {
    return parent.inflater().inflate(this, parent, attachToRoot)
}

fun Int.bindInflater(parent: ViewGroup, attachToRoot: Boolean = false): ViewDataBinding {
    return DataBindingUtil.inflate(parent.inflater(), this, parent, attachToRoot)
}

fun View?.setOnSafeClickListener(
    onSafeClick: (View) -> Unit
): View? {
    this?.setOnClickListener(SafeClickListener { v ->
        onSafeClick(v)
    })
    return this
}

fun View?.setOnSafeClickListener(
    interval: Int,
    onSafeClick: (View) -> Unit
): View? {
    this?.setOnClickListener(SafeClickListener(interval, { v ->
        onSafeClick(v)
    }))
    return this
}
