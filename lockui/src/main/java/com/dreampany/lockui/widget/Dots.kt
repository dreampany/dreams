package com.dreampany.lockui.widget

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef
import androidx.appcompat.widget.LinearLayoutCompat
import com.dreampany.common.extensions.dimension
import com.dreampany.lockui.R

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Dots : LinearLayoutCompat {

    @IntDef(DotType.FIXED, DotType.FILL, DotType.FILL_WITH_ANIMATION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DotType {
        companion object {
            const val FIXED = 0
            const val FILL = 1
            const val FILL_WITH_ANIMATION = 2
        }
    }

    private val DEFAULT_PIN_LENGTH = 4
    private val DEFAULT_ANIMATION_DURATION = 200L

    private var pinLength = 0
    private var previousPinLength = 0
    private var dotDiameter = 0
    private var dotSpacing = 0

    private var fillDrawable = 0
    private var emptyDrawable = 0
    private var dotType = 0

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initUi(context, attrs)
        updateUi(context)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (dotType != DotType.FIXED) {
            layoutParams.height = dotDiameter
            requestLayout()
        }
    }

    fun setDotType(@DotType dotType: Int) {
        this.dotType = dotType
        removeAllViews()
        updateUi(context)
    }

    fun setPinLength(pinLength: Int) {
        this.pinLength = pinLength
        removeAllViews()
        updateUi(context)
    }

    private fun initUi(context: Context, attrs: AttributeSet?) {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.lockui)
        try {
            pinLength = array.getInt(R.styleable.lockui_pinLength, DEFAULT_PIN_LENGTH)
            dotDiameter = array.getDimension(
                R.styleable.lockui_dotDiameter, context.dimension(R.dimen.dot_diameter)
            ) as Int
            dotSpacing = array.getDimension(
                R.styleable.lockui_dotSpacing, context.dimension(R.dimen.dot_spacing)
            ) as Int

            fillDrawable =
                array.getResourceId(R.styleable.lockui_dotFilledBackground, R.drawable.dot_filled)
            emptyDrawable =
                array.getResourceId(R.styleable.lockui_dotFilledBackground, R.drawable.dot_empty)
            dotType = array.getInt(R.styleable.lockui_dotType, DotType.FIXED)
        } finally {
            array.recycle()
        }
    }

    private fun updateUi(context: Context) {
        when (dotType) {
            DotType.FIXED -> {
                for (index in 1..pinLength) {
                    val dot = View(context)
                    emptyDot(dot)

                    val params = LayoutParams(dotDiameter, dotDiameter)
                    params.setMargins(dotSpacing, 0, dotSpacing, 0)
                    dot.layoutParams = params

                    addView(dot)
                }
            }
            DotType.FILL_WITH_ANIMATION -> {
                val transition = LayoutTransition()
                transition.setDuration(DEFAULT_ANIMATION_DURATION)
                transition.setStartDelay(LayoutTransition.APPEARING, 0)
                layoutTransition = transition
            }
        }
    }

    private fun updateDot(length: Int) {
        if (dotType == DotType.FIXED) {
            if (length > 0) {
                if (length > previousPinLength) {
                    fillDot(getChildAt(length - 1))
                } else {
                    emptyDot(getChildAt(length))
                }
                previousPinLength = length
            } else {
                for (index in 0..childCount - 1) {
                    emptyDot(getChildAt(index))
                }
                previousPinLength = 0
            }
            return
        }

        if (length > 0) {
            if (length > previousPinLength) {
                val dot = View(context)
                fillDot(dot)

                val params = LayoutParams(dotDiameter, dotDiameter)
                params.setMargins(dotSpacing, 0, dotSpacing, 0)
                dot.layoutParams = params

                addView(dot, length - 1)
            } else {
                removeViewAt(length)
            }
            previousPinLength = length
        } else {
            removeAllViews()
            previousPinLength = 0
        }
    }

    private fun emptyDot(dot: View) {
        dot.setBackgroundResource(emptyDrawable)
    }

    private fun fillDot(dot: View) {
        dot.setBackgroundResource(fillDrawable)
    }
}