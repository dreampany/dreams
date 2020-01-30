package com.dreampany.framework.misc.extension

import android.content.Context
import android.graphics.PorterDuff
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.framework.R
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.misc.func.SafeClickListener

/**
 * Created by roman on 2019-09-27
 * Copyright (c) 2019 bjit. All rights reserved.
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

fun EditText?.isEmpty(): Boolean {
    return this?.text?.trim().isNullOrEmpty() ?: false
}

fun EditText?.rawText(): String? {
    return this?.text?.trim()?.toString()
}

fun Fragment?.resolveText(text: String? = Constants.Default.NULL): String {
    return if (!text.isNullOrEmpty()) text else Constants.Default.STRING
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

fun MenuItem?.toTint(@Nullable context: Context?, @ColorRes colorRes: Int): MenuItem? {
    if (context == null) return this
    this?.icon?.mutate()?.setColorFilter(colorRes.toColor(context), PorterDuff.Mode.SRC_ATOP)
    return this
}

fun Int.toColor(@NonNull context: Context): Int {
    return ContextCompat.getColor(context, this)
}

fun SwipeRefreshLayout?.bind(listener: SwipeRefreshLayout.OnRefreshListener?): SwipeRefreshLayout? {
    this?.setColorSchemeResources(
        R.color.colorPrimary,
        R.color.colorAccent,
        R.color.colorPrimaryDark
    )
    listener?.let {
        this?.setOnRefreshListener(it)
    }
    return this
}