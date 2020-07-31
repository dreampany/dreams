package com.dreampany.framework.misc.exts

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.dreampany.framework.misc.func.SafeClickListener
import com.dreampany.adapter.FlexibleItemDecoration
import com.dreampany.framework.R
import com.google.android.material.textfield.TextInputEditText

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

val View?.isNull: Boolean get() = this == null
val View?.isNotNull: Boolean get() = this != null

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.visible(visible: Boolean) {
    if (visible) visible()
    else gone()
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.enable() {
    this?.isEnabled = true
}

fun View?.disable() {
    this?.isEnabled = false
}

const val FLAGS_FULLSCREEN = View.SYSTEM_UI_FLAG_LOW_PROFILE or
        View.SYSTEM_UI_FLAG_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)

val ViewGroup.inflater: LayoutInflater get() = context.inflater

fun Context.inflate(@LayoutRes layoutRes: Int): View = inflater.inflate(layoutRes, null)
fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = inflater.inflate(layoutRes, null)
//fun ViewGroup.bindInflater()

fun Int.bindInflater(parent: ViewGroup, attachToRoot: Boolean = false): ViewDataBinding {
    return DataBindingUtil.inflate(parent.inflater, this, parent, attachToRoot)
}

fun <T : ViewDataBinding> Context.bindInflater(
    @LayoutRes layoutRes: Int,
    parent: ViewGroup? = null
): T {
    return DataBindingUtil.inflate(inflater, layoutRes, parent, parent != null)
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

fun ImageView?.toTint(@ColorRes colorRes: Int): ImageView? {
    this?.setColorFilter(this.context.color(colorRes))
    return this
}

fun Drawable?.toTint(@Nullable context: Context?, @ColorRes colorRes: Int): Drawable? {
    if (context == null) return this
    this?.setColorFilter(
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.color(colorRes), BlendModeCompat.SRC_ATOP
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

fun TextView.blink(@ColorRes startColorRes: Int, @ColorRes endColorRes: Int) {
    val startColor = context.color(startColorRes)
    val endColor = context.color(endColorRes)
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

val ViewBinding.context: Context get() = root.context

fun ViewBinding.color(@ColorRes resId: Int): Int = context.color(resId)

fun ViewBinding.string(@StringRes resId: Int): String = context.getString(resId)

fun SwipeRefreshLayout?.init(
    listener: SwipeRefreshLayout.OnRefreshListener,
    @ColorRes vararg colorResIds: Int
) {
    this?.apply {
        //setColorSchemeResources(colorResIds)
        setOnRefreshListener(listener)
        setColorSchemeResources(
            R.color.material_red500,
            R.color.material_pink500,
            R.color.material_purple500,
            R.color.material_deeppurple500,
            R.color.material_indigo500,
            R.color.material_blue500,
            R.color.material_lightblue500,
            R.color.material_cyan500,
            R.color.material_teal500,
            R.color.material_green500,
            R.color.material_lightgreen500,
            R.color.material_lime500,
            R.color.material_yellow500,
            R.color.material_amber500,
            R.color.material_orange500,
            R.color.material_deeporange500,
            R.color.material_brown500,
            R.color.material_grey500,
            R.color.material_bluegrey400
        )
    }
}

fun SwipeRefreshLayout?.refresh(refresh: Boolean) = if (refresh) this.showRefresh()
else this.hideRefresh()

fun SwipeRefreshLayout?.showRefresh() = this?.apply {
    if (!isRefreshing)
        post({
            isRefreshing = true
        })

}

fun SwipeRefreshLayout?.hideRefresh() = this?.apply {
    if (isRefreshing)
        post({
            isRefreshing = false
        })
}

inline fun View.afterMeasured(crossinline block: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}

fun Context?.spanHeight(spans: Int, offset: Int): Int =
    (screenWidth / spans) - (dpToPx(offset.toFloat()) * spans)
