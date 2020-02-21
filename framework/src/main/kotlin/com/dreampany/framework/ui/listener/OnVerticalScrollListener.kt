package com.dreampany.framework.ui.listener

import android.os.Handler
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by roman on 2020-02-22
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class OnVerticalScrollListener(var scrollingCallAtEnd: Boolean = false) :
    RecyclerView.OnScrollListener() {

    private val delay = 3000
    private var scrolling = false
    private val handler: Handler? = null
    private val runner = Runnable { onScrollingAtEnd() }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrolling = false
            onIdle()
        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            scrolling = true
            onScrolling()
            if (scrollingCallAtEnd) {
                handler?.removeCallbacks(runner)
                handler?.postDelayed(runner, delay.toLong())
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop()
        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom()
        }
        if (dy < 0) {
            onScrolledUp(dy)
        } else if (dy > 0) {
            onScrolledDown(dy)
        }
    }

    fun onScrolledUp(dy: Int) {
        onScrolledUp()
    }

    fun onScrolledDown(dy: Int) {
        onScrolledDown()
    }

    fun isScrolling(): Boolean {
        return scrolling
    }

    fun isIdle(): Boolean {
        return !scrolling
    }

    open fun onIdle() {

    }

    open fun onScrolling() {}

    open fun onScrollingAtEnd() {}

    open fun onScrolledUp() {}

    open fun onScrolledDown() {}

    open fun onScrolledToTop() {}

    open fun onScrolledToBottom() {}
}