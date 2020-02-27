package com.dreampany.framework.misc.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun Int.resToStringArray(context: Context?): Array<String>? {
    //if (context == null) return null
    return context?.resources?.getStringArray(this)
}

fun Int.singleItemOfStringResArray(context: Context?, index: Int): String? {
    //if (context == null) return null
    val array = context?.resources?.getStringArray(this) ?: return null
   return if (index < 0 || index >= array.size)  null else array.get(index)
}

fun Int.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().getDisplayMetrics()
    ).toInt()
}

fun Context?.inflate(
    @LayoutRes resId: Int, parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View? {
    if (this == null) return null
    return LayoutInflater.from(this).inflate(resId, parent, attachToRoot)
}

fun Int.toColor(@Nullable context: Context?): Int {
    if (context == null) return 0
    return ContextCompat.getColor(context, this)
}

fun String.toColor(): Int {
    return Color.parseColor(this)
}

fun Context?.showToast(@StringRes resId: Int): Context? {
    if (this == null) return this
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    return this
}

fun Context?.showToast(text: String?): Context? {
    if (this == null || text.isNullOrEmpty()) return this
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    return this
}

fun Long.expired(delay: Long): Boolean {
    return System.currentTimeMillis() - this > delay
}

fun <T> List<T>.secondOrNull(): T? {
    return if (size < 2) null else this[1]
}

fun <T> List<T>.thirdOrNull(): T? {
    return if (size < 3) null else this[2]
}

fun <T> List<T>.fourthOrNull(): T? {
    return if (size < 4) null else this[3]
}

fun Long.format(pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

fun Boolean?.rawBool(): Boolean {
    if (this == null) return false
    return this
}

fun Context?.statusBarHeightInPx(): Int {
    if (this == null) return 0
    val resourceId: Int = getResources().getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return getResources().getDimensionPixelSize(resourceId)
    }
    return 0
}

fun Context?.screenWidthInPx(): Int {
    if (this == null) return 0
    val dm = DisplayMetrics()
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

fun Context?.screenHeightInPx(): Int {
    if (this == null) return 0
    val dm = DisplayMetrics()
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getMetrics(dm)
    return dm.heightPixels
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}


fun Int.pixelsToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Context?.inflater(): LayoutInflater? {
    if (this == null) return null
    return LayoutInflater.from(this)
}

fun ViewGroup.inflater(): LayoutInflater {
    return LayoutInflater.from(context)
}

fun <T : ViewDataBinding> ViewGroup.bindInflater(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): T {
    return DataBindingUtil.inflate(inflater(), layoutRes, this, attachToRoot)
}

fun Int.bindInflater(parent: ViewGroup, attachToRoot: Boolean = false): ViewDataBinding {
    return DataBindingUtil.inflate(parent.inflater(), this, parent, attachToRoot)
}

fun Int.isZeroOrLess(): Boolean {
    return this <= 0
}

fun Int?.resolve(): Int {
    return if (this == null) 0 else this
}

fun Long?.resolve(): Long {
    return if (this == null) 0L else this
}

fun String?.toHtml(): Spanned? {
    if (this.isNullOrEmpty()) return null
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

