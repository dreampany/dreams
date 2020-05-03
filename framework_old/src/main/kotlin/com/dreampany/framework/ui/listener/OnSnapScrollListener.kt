package com.dreampany.framework.ui.listener

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.dreampany.framework.misc.extensions.getSnapPosition

/**
 * Created by roman on 2020-02-22
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class OnSnapScrollListener(
    private val snapHelper: SnapHelper,
    var behavior: Behavior = Behavior.ON_SCROLL,
    var onSnapListener: OnSnapChangeListener? = null
) : RecyclerView.OnScrollListener() {

    enum class Behavior {
        STATE_IDLE,
        ON_SCROLL
    }

    interface OnSnapChangeListener {
        fun onSnapChange(position: Int)
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (behavior == Behavior.ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (behavior == Behavior.STATE_IDLE
            && newState == RecyclerView.SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    open fun onSnapChange(position: Int) {

    }

    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            onSnapListener?.onSnapChange(snapPosition)
            onSnapChange(snapPosition)
            this.snapPosition = snapPosition
        }
    }

}