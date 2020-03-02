/*
package com.dreampany.framework.api.ui.fab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import com.dreampany.framework.R
import mbanje.kurt.fabbutton.FabUtil


*/
/**
 * Created by roman on 2019-11-02
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 *//*

class ProgressRingView : View, FabUtil.OnFabValueCallback {

    private var progress: Float = 0.toFloat()
    private var maxProgress: Float = 0.toFloat()
    private var indeterminateSweep: Float = 0.toFloat()
    private var indeterminateRotateOffset: Float = 0.toFloat()

    private var progressColor = Color.BLACK

    private var size = 0
    private lateinit var bounds: RectF

    private var ringWidth: Int = 0
    private var midRingWidth: Int = 0
    private var animDuration: Int = 0

    private var boundsPadding = 0.14f
    private var viewRadius: Int = 0
    private var ringWidthRatio = 0.14f //of a possible 1f

    private lateinit var progressPaint: Paint

    private var indeterminate: Boolean = false
    private var autostartanim: Boolean = false

    // Animation related stuff
    private var startAngle: Float = 0.toFloat()
    private var actualProgress: Float = 0.toFloat()
    private var startAngleRotate: ValueAnimator? = null
    private var progressAnimator: ValueAnimator? = null
    private var indeterminateAnimator: AnimatorSet? = null

    private var fabViewListener: CircleImageView.OnFabViewListener? = null

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr)
    }

    private
    fun init(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0)
        progress = a.getFloat(R.styleable.CircleImageView_android_progress, 0f)
        progressColor = a.getColor(R.styleable.CircleImageView_fbb_progressColor, progressColor)
        maxProgress = a.getFloat(R.styleable.CircleImageView_android_max, 100f)
        indeterminate = a.getBoolean(R.styleable.CircleImageView_android_indeterminate, false)
        autostartanim = a.getBoolean(R.styleable.CircleImageView_fbb_autoStart, true)
        animDuration = a.getInteger(R.styleable.CircleImageView_android_indeterminateDuration, 4000)
        ringWidthRatio =
            a.getFloat(R.styleable.CircleImageView_fbb_progressWidthRatio, ringWidthRatio)
        a.recycle()

        progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progressPaint.setColor(progressColor)
        progressPaint.setStyle(Paint.Style.STROKE)
        progressPaint.setStrokeCap(Paint.Cap.BUTT)

        if (autostartanim) {
            startAnimation()
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        size = Math.min(w, h)
        viewRadius = size / 2
        setRingWidth(-1, true)
    }

    override fun onDraw(canvas: Canvas) {
        // Draw the arc
        val sweepAngle =
            if (isInEditMode) progress / maxProgress * 360 else actualProgress / maxProgress * 360
        if (!indeterminate) {
            canvas.drawArc(bounds, startAngle, sweepAngle, false, progressPaint)
        } else {
            canvas.drawArc(
                bounds,
                startAngle + indeterminateRotateOffset,
                indeterminateSweep,
                false,
                progressPaint
            )
        }
    }

    override fun onIndeterminateValuesChanged(
        indeterminateSweep: Float,
        indeterminateRotateOffset: Float,
        startAngle: Float,
        progress: Float
    ) {
        if (indeterminateSweep != -1f) {
            this.indeterminateSweep = indeterminateSweep
        }
        if (indeterminateRotateOffset != -1f) {
            this.indeterminateRotateOffset = indeterminateRotateOffset
        }
        if (startAngle != -1f) {
            this.startAngle = startAngle
        }
        if (progress != -1f) {
            this.actualProgress = progress
            if (Math.round(actualProgress) === 100 && fabViewListener != null) {
                fabViewListener?.onProgressCompleted()
            }
        }
    }

    fun setRingWidthRatio(ringWidthRatio: Float) {
        this.ringWidthRatio = ringWidthRatio
    }

    fun setAutostartanim(autostartanim: Boolean) {
        this.autostartanim = autostartanim
    }

    fun setFabViewListener(fabViewListener: CircleImageView.OnFabViewListener) {
        this.fabViewListener = fabViewListener
    }

    fun setRingWidth(width: Int, original: Boolean) {
        if (original) {
            ringWidth = Math.round(viewRadius as Float * ringWidthRatio)
        } else {
            ringWidth = width
        }
        midRingWidth = ringWidth / 2
        progressPaint.strokeWidth = ringWidth.toFloat()
        updateBounds()
    }

    private fun updateBounds() {
        bounds = RectF(
            midRingWidth.toFloat(),
            midRingWidth.toFloat(),
            (size - midRingWidth).toFloat(),
            (size - midRingWidth).toFloat()
        )
    }

    */
/**
     * Sets the progress of the progress bar.
     * @param currentProgress the current progress you want to set
     *//*

    fun setProgress(currentProgress: Float) {
        this.progress = currentProgress
        // Reset the determinate animation to approach the new progress
        if (!indeterminate) {
            if (progressAnimator != null && progressAnimator!!.isRunning()) {
                progressAnimator?.cancel()
            }
            progressAnimator =
                FabUtil.createProgressAnimator(this, actualProgress, currentProgress, this)
            progressAnimator?.start()
        }
        invalidate()

    }


    fun setMaxProgress(maxProgress: Float) {
        this.maxProgress = maxProgress
    }


    fun setIndeterminate(indeterminate: Boolean) {
        this.indeterminate = indeterminate
    }

    fun setAnimDuration(animDuration: Int) {
        this.animDuration = animDuration
    }


    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
        progressPaint.color = progressColor
    }


    */
/**
     * Starts the progress bar animation.
     * (This is an alias of resetAnimation() so it does the same thing.)
     *//*

    fun startAnimation() {
        resetAnimation()
    }


    fun stopAnimation(hideProgress: Boolean) {
        if (startAngleRotate != null && startAngleRotate!!.isRunning()) {
            startAngleRotate?.cancel()
        }
        if (progressAnimator != null && progressAnimator!!.isRunning()) {
            progressAnimator?.cancel()
        }
        if (indeterminateAnimator != null && indeterminateAnimator!!.isRunning()) {
            indeterminateAnimator?.cancel()
        }
        if (hideProgress) {
            setRingWidth(0, false)
        } else {
            setRingWidth(0, true)
        }
        invalidate()
    }

    */
/**
     * Resets the animation.
     *//*

    fun resetAnimation() {
        stopAnimation(false)
        // Determinate animation
        if (!indeterminate) {
            // The cool 360 swoop animation at the start of the animation
            startAngle = -90f
            startAngleRotate = FabUtil.createStartAngleAnimator(this, -90f, 270f, this)
            startAngleRotate?.start()
            // The linear animation shown when progress is updated
            actualProgress = 0f
            progressAnimator = FabUtil.createProgressAnimator(this, actualProgress, progress, this)
            progressAnimator?.start()
        } else { // Indeterminate animation
            startAngle = -90f
            indeterminateSweep = FabUtil.INDETERMINANT_MIN_SWEEP
            // Build the whole AnimatorSet
            indeterminateAnimator = AnimatorSet()
            var prevSet: AnimatorSet? = null
            var nextSet: AnimatorSet
            for (k in 0 until FabUtil.ANIMATION_STEPS) {
                nextSet = FabUtil.createIndeterminateAnimator(this, k.toFloat(), animDuration, this)
                val builder = indeterminateAnimator?.play(nextSet)
                if (prevSet != null) {
                    builder?.after(prevSet)
                }
                prevSet = nextSet
            }

            // Listen to end of animation so we can infinitely loop
            indeterminateAnimator?.addListener(object : AnimatorListenerAdapter() {
                internal var wasCancelled = false
                override fun onAnimationCancel(animation: Animator) {
                    wasCancelled = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (!wasCancelled) {
                        resetAnimation()
                    }
                }
            })
            indeterminateAnimator?.start()
        }
    }
}*/
