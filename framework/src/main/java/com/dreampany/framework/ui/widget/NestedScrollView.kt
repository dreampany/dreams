package com.dreampany.framework.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by roman on 9/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NestedScrollView : androidx.core.widget.NestedScrollView {
    private var scrollable = false

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun setScrollable(scrollable: Boolean) {
        this.scrollable = scrollable
    }

    fun isScrollable(): Boolean {
        return scrollable
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> scrollable && super.onTouchEvent(ev)
            else -> super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable && super.onInterceptTouchEvent(ev)
    }
}
