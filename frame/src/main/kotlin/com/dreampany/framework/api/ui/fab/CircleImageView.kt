package com.dreampany.framework.api.ui.fab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import com.dreampany.framework.R
import mbanje.kurt.fabbutton.CircleImageView.OnFabViewListener


/**
 * Created by roman on 2019-11-02
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CircleImageView : AppCompatImageView {

    private val animationDuration = 200

    private lateinit var circlePaint: Paint
    private var circleRadius: Int = 0

    private lateinit var ringPaint: Paint
    private var currentRingWidth: Float = 0f
    private var ringWidthRatio = 0.14f //of a possible 1f;
    private var ringAlpha = 75
    private var ringRadius: Int = 0
    private var ringWidth: Int = 0

    private var shadowDy = 3.5f
    private var shadowDx = 0f
    private var shadowRadius = 10f

    private var centerY: Int = 0
    private var centerX: Int = 0
    private var viewRadius: Int = 0

    private var shadowTransparency = 100
    private var showEndBitmap: Boolean = false
    private var showShadow = true

    private var progressVisible: Boolean = false

    private val drawables = arrayOfNulls<Drawable>(2)
    private var crossfader: TransitionDrawable? = null


    private lateinit var ringAnimator: ObjectAnimator

    private var fabViewListener: OnFabViewListener? = null

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    override protected fun onDraw(canvas: Canvas) {
        val ringR = ringRadius + currentRingWidth
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), ringR, ringPaint) // the outer ring
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), circleRadius.toFloat(), circlePaint) //the actual circle
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        viewRadius = Math.min(w, h) / 2
        ringWidth = Math.round(viewRadius as Float * ringWidthRatio)
        circleRadius = viewRadius - ringWidth
        ringPaint.strokeWidth = ringWidth.toFloat()
        ringPaint.alpha = ringAlpha
        ringRadius = circleRadius - ringWidth / 2
    }

    fun setFabViewListener(fabViewListener: OnFabViewListener) {
        this.fabViewListener = fabViewListener
    }

    fun init(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) {
        setFocusable(false)
        setScaleType(ScaleType.CENTER_INSIDE)
        setClickable(true)
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint.setStyle(Paint.Style.FILL)
        ringPaint = Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE)

        val displayMetrics = resources.displayMetrics
        if (displayMetrics.densityDpi <= 240) {
            shadowRadius = 1.0f
        } else if (displayMetrics.densityDpi <= 320) {
            shadowRadius = 3.0f
        } else {
            shadowRadius = 10.0f
        }

        var color = Color.BLACK
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            color = a.getColor(R.styleable.CircleImageView_android_color, Color.BLACK)
            ringWidthRatio =
                a.getFloat(R.styleable.CircleImageView_fbb_progressWidthRatio, ringWidthRatio)
            shadowRadius =
                a.getFloat(R.styleable.CircleImageView_android_shadowRadius, shadowRadius)
            shadowDy = a.getFloat(R.styleable.CircleImageView_android_shadowDy, shadowDy)
            shadowDx = a.getFloat(R.styleable.CircleImageView_android_shadowDx, shadowDx)
            setShowShadow(a.getBoolean(R.styleable.CircleImageView_fbb_showShadow, true))
            a.recycle();
        }

        setColor(color)
        val pressedAnimationTime = animationDuration
        ringAnimator = ObjectAnimator.ofFloat(this, "currentRingWidth", 0f, 0f)
        ringAnimator.setDuration(pressedAnimationTime.toLong())
        ringAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (fabViewListener != null) {
                    fabViewListener?.onProgressVisibilityChanged(progressVisible)
                }
            }
        })
    }

    fun setShowShadow(showShadow: Boolean) {
        if (showShadow) {
            circlePaint.setShadowLayer(
                shadowRadius,
                shadowDx,
                shadowDy,
                Color.argb(shadowTransparency, 0, 0, 0)
            )
        } else {
            circlePaint.clearShadowLayer()
        }
        invalidate()
    }

    fun setShowEndBitmap(showEndBitmap: Boolean) {
        this.showEndBitmap = showEndBitmap
    }

    /**
     * sets the icon that will be shown on the fab icon
     *
     * @param resource the resource id of the icon
     */
    fun setIcon(resource: Int, endBitmapResource: Int) {
        val srcBitmap = BitmapFactory.decodeResource(resources, resource)
        if (showEndBitmap) {
            val endBitmap = BitmapFactory.decodeResource(resources, endBitmapResource)
            setIconAnimation(
                BitmapDrawable(resources, srcBitmap),
                BitmapDrawable(resources, endBitmap)
            )
        } else {
            setImageBitmap(srcBitmap)
        }
    }

    /**
     * sets the icon that will be shown on the fab icon
     *
     * @param icon the initial icon
     * @param endIcon the icon to be displayed when the progress is finished
     */
    fun setIcon(icon: Drawable, endIcon: Drawable) {
        if (showEndBitmap) {
            setIconAnimation(icon, endIcon)
        } else {
            setImageDrawable(icon)
        }
    }

    private fun setIconAnimation(icon: Drawable, endIcon: Drawable) {
        drawables[0] = icon
        drawables[1] = endIcon
        crossfader = TransitionDrawable(drawables)
        crossfader?.isCrossFadeEnabled = true
        setImageDrawable(crossfader)
    }

    fun resetIcon() {
        crossfader?.resetTransition()
    }

    /**
     * this sets the thickness of the ring as a fraction of the radius of the circle.
     *
     * @param ringWidthRatio the ratio 0-1
     */
    fun setRingWidthRatio(ringWidthRatio: Float) {
        this.ringWidthRatio = ringWidthRatio
    }

    fun getCurrentRingWidth(): Float {
        return currentRingWidth
    }

    fun setCurrentRingWidth(currentRingWidth: Float) {
        this.currentRingWidth = currentRingWidth
        this.invalidate()
    }

    /**
     * sets the color of the circle
     *
     * @param color the actual color to set to
     */
    fun setColor(color: Int) {
        circlePaint.color = color
        ringPaint.color = color
        ringPaint.alpha = ringAlpha
        this.invalidate()
    }

    /**
     * whether to show the ring or not
     *
     * @param show set flag
     */
    fun showRing(show: Boolean) {
        progressVisible = show
        if (show) {
            ringAnimator.setFloatValues(currentRingWidth, ringWidth.toFloat())
        } else {
            ringAnimator.setFloatValues(ringWidth.toFloat(), 0f)
        }
        ringAnimator.start()
    }

    /**
     * this animates between the icon set in the imageview and the completed icon. does as crossfade animation
     *
     * @param show set flag
     * @param hideOnComplete if true animate outside ring out after progress complete
     */
    fun showCompleted(show: Boolean, hideOnComplete: Boolean) {
        if (show) {
            crossfader?.startTransition(500)
        }
        if (hideOnComplete) {
            val hideAnimator = ObjectAnimator.ofFloat(this, "currentRingWidth", 0f, 0f)
            hideAnimator.setFloatValues(1.toFloat())
            hideAnimator.duration = animationDuration.toLong()
            hideAnimator.start()

        }
    }
}