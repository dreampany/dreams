package com.dreampany.common.misc.extension

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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

/*fun Int.inflater(parent: ViewGroup, attachToRoot: Boolean = false): View {
    return parent.inflater().inflate(this, parent, attachToRoot)
}*/

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

fun TextInputEditText.toString() : String {
    return this.text?.trim().toString()
}
