package com.dreampany.framework.misc.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dreampany.framework.R
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.misc.func.SafeClickListener
import com.dreampany.framework.ui.adapter.SmartAdapter


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

fun EditText?.string(): String? {
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

fun MenuItem?.toTint(@Nullable context: Context?, @ColorRes colorRes: Int): MenuItem? {
    this?.icon?.mutate()?.toTint(context, colorRes)
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

fun RecyclerView?.apply(
    adapter: SmartAdapter<*>,
    layout: RecyclerView.LayoutManager,
    fixedSize: Boolean = true,
    decoration: RecyclerView.ItemDecoration? = null,
    animator: RecyclerView.ItemAnimator? = null,
    scroller: RecyclerView.OnScrollListener? = null) {
    layout.isItemPrefetchEnabled = false
    this?.apply {
        setHasFixedSize(fixedSize)
        setAdapter(adapter)
        layoutManager = layout
        if (decoration != null && itemDecorationCount == 0) {
            addItemDecoration(decoration)
        }
        if (animator != null) {
            itemAnimator = animator
        } else {
            //(Objects.requireNonNull(recycler.itemAnimator) as DefaultItemAnimator).supportsChangeAnimations = false
            //recycler.setItemAnimator(null);
        }
        clearOnScrollListeners()
        if (scroller != null) {
            addOnScrollListener(scroller)
        }
    }
}

fun RecyclerView?.currentPosition() : Int {
    if (this == null) return -1
    val layout = this.layoutManager
    if (layout is LinearLayoutManager) {
        return layout.findFirstVisibleItemPosition()
    } else if (layout is GridLayoutManager) {
        return layout.findFirstVisibleItemPosition()
    } else if (layout is StaggeredGridLayoutManager) {
        return -1
    }
    return -1
}

fun SnapHelper.getSnapPosition(recycler: RecyclerView): Int {
    val layoutManager = recycler.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}