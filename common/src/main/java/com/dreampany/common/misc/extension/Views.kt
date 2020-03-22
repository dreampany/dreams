package com.dreampany.common.misc.extension

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.misc.func.SafeClickListener
import com.google.android.material.textfield.TextInputEditText

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun View?.isNull(): Boolean {
    return this == null
}

fun View?.isNotNull(): Boolean {
    return this != null
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun Context.inflater(): LayoutInflater {
    return LayoutInflater.from(this)
}

fun ViewGroup.inflater(): LayoutInflater {
    return context.inflater()
}

//fun ViewGroup.bindInflater()

fun Int.bindInflater(parent: ViewGroup, attachToRoot: Boolean = false): ViewDataBinding {
    return DataBindingUtil.inflate(parent.inflater(), this, parent, attachToRoot)
}

fun <T : ViewDataBinding> Context.bindInflater(
    @LayoutRes layoutRes: Int,
    parent: ViewGroup? = null
): T {
    return DataBindingUtil.inflate(inflater(), layoutRes, parent, parent != null)
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

fun View?.loadAnim(@AnimRes animRes: Int): View? {
    if (this == null) return this
    this.startAnimation(AnimationUtils.loadAnimation(context, animRes))
    return this
}

fun TextInputEditText.isEmpty() : Boolean {
    return this.text.isNullOrEmpty()
}

fun TextInputEditText.string() : String {
    return this.text?.trim().toString()
}

fun RecyclerView.addDivider(dimen: Int) {
    val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

    divider.setDrawable(ShapeDrawable().apply {
        intrinsicHeight = dimen
        paint.color = Color.TRANSPARENT // note: currently (support version 28.0.0), we can not use tranparent color here, if we use transparent, we still see a small divider line. So if we want to display transparent space, we can set color = background color or we can create a custom ItemDecoration instead of DividerItemDecoration.
    })
    addItemDecoration(divider)
}
