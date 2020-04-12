package com.dreampany.common.misc.extension

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.misc.func.SafeClickListener
import com.dreampany.adapter.FlexibleItemDecoration
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

fun MenuItem?.toTint(@Nullable context: Context?, @ColorRes colorRes: Int): MenuItem? {
    this?.icon?.mutate()?.toTint(context, colorRes)
    return this
}

fun Drawable?.toTint(@Nullable context: Context?, @ColorRes colorRes: Int): Drawable? {
    if (context == null) return this
/*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

    } else {
        this?.setColorFilter(colorRes.toColor(context), PorterDuff.Mode.SRC_ATOP)
    }*/
    this?.setColorFilter(
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            colorRes.toColor(
                context
            ), BlendModeCompat.SRC_ATOP
        )
    )
    return this
}

fun TextInputEditText.isEmpty(): Boolean {
    return this.text.isNullOrEmpty()
}

fun TextInputEditText.string(): String {
    return this.text?.trim().toString()
}

fun RecyclerView.addDecoration(offset: Int) {
    val decoration = FlexibleItemDecoration(context).withOffset(offset).withEdge(true)
    addItemDecoration(decoration)
}

fun TextView.blink(@ColorRes startColorId: Int,@ColorRes endColorId: Int) {
    val startColor =startColorId.toColor(context)
    val endColor = endColorId.toColor(context)
    val animator = ObjectAnimator.ofInt(this, "textColor", startColor, endColor, startColor)
    animator.duration = 1500
    animator.setEvaluator(ArgbEvaluator())
    animator.repeatMode = ValueAnimator.REVERSE
    animator.repeatCount = ValueAnimator.RESTART
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {
           setTextColor(endColor)
        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {

        }
    })
    animator.start()

}
