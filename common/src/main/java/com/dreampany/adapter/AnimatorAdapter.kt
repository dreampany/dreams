package com.dreampany.adapter

import android.animation.Animator
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.animation.LinearInterpolator
import androidx.annotation.IntRange
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by roman on 11/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class AnimatorAdapter<VH : RecyclerView.ViewHolder>(val stableIds: Boolean = false) :
    SelectableAdapter<VH>() {

    private val DEFAULT_DURATION = 300L

    protected var interpolator = LinearInterpolator()
    private val animatorObserver: AnimatorDataObserver
    private val animators: SparseArrayCompat<Animator>
    private val animatorsUsed: EnumSet<Type>

    private var lastAnimatedPosition = -1
    private var maxChildViews = -1
    protected var entryStep = true

    private var isReverseEnabled = false
    private var isForwardEnabled = false
    private var onlyEntryAnimation = false

    protected var animateFromObserver = false
    protected var initialDelay = 0L

    @IntRange(from = 0)
    protected var stepDelay = 100L

    @IntRange(from = 1)
    protected var duration: Long = DEFAULT_DURATION

    enum class Type {
        ALPHA, SLIDE_IN_LEFT, SLIDE_IN_RIGHT, SLIDE_IN_BOTTOM, SLIDE_IN_TOP, SCALE
    }

    init {
        setHasStableIds(stableIds)
        animatorObserver = AnimatorDataObserver()
        registerAdapterDataObserver(animatorObserver)
        animators = SparseArrayCompat()
        animatorsUsed = EnumSet.noneOf(Type::class.java)
    }

    protected fun animate(holder: VH, position: Int) {
        val recycler = super.recycler ?: return
        if (maxChildViews < recycler.childCount)
            maxChildViews = recycler.childCount

        if (onlyEntryAnimation && lastAnimatedPosition >= maxChildViews)
            isForwardEnabled = false
        val lastVisiblePosition = layoutManager
    }

    private fun cancelExistingAnimation(hashCode: Int) {
        animators.get(hashCode)?.end()
    }

    inner class AnimatorDataObserver : RecyclerView.AdapterDataObserver() {
        var notified = false
        private val handler = Handler(Looper.getMainLooper(), object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                notified = false
                return true
            }
        })

        override fun onChanged() {
            notified = true
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            notified = true
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            notified = true
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            notified = true
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            notified = true
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            notified = true
        }

        fun clearNotify() {
            if (notified) {
                handler.removeCallbacksAndMessages(null)
                handler.sendMessageDelayed(Message.obtain(handler), 200L)
            }
        }
    }

    inner class HelperAnimatorListener(key: Int) : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            TODO("Not yet implemented")
        }

        override fun onAnimationEnd(animation: Animator?) {
            TODO("Not yet implemented")
        }

        override fun onAnimationCancel(animation: Animator?) {
            TODO("Not yet implemented")
        }

        override fun onAnimationStart(animation: Animator?) {
            TODO("Not yet implemented")
        }

    }
}