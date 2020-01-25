package com.dreampany.framework.misc.extension

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.framework.R
import com.dreampany.framework.misc.func.SafeClickListener

/**
 * Created by roman on 2019-10-26
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

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