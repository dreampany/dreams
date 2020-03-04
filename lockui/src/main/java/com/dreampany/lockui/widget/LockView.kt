package com.dreampany.lockui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.extensions.color
import com.dreampany.common.extensions.dimension
import com.dreampany.common.misc.Constants
import com.dreampany.lockui.R
import com.dreampany.lockui.ui.adapter.LockAdapter

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LockView : RecyclerView {

    private val DEFAULT_PIN_LENGTH = 4
    private val DEFAULT_KEY_SET = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    private var pin: String = Constants.Default.STRING
    private var pinLength: Int = 0
    private var horizontalSpacing: Int = 0
    private var verticalSpacing: Int = 0

    private var textColor: Int = 0
    private var deleteButtonPressedColor: Int = 0
    private var textSize: Int = 0
    private var buttonSize: Int = 0
    private var deleteButtonWidthSize: Int = 0
    private var deleteButtonHeightSize: Int = 0

    private var buttonBackgroundDrawable: Drawable? = null
    private var deleteButtonDrawable: Drawable? = null
    private var showDeleteButton = false

    private lateinit var dots: Dots
    private lateinit var lockAdapter: LockAdapter
    private lateinit var pinLockAdapter: LockAdapter

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
    }

    private fun initUi(context: Context, attrs: AttributeSet?) {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.lockui)
        try {
            pinLength = array.getInt(R.styleable.lockui_pinLength, DEFAULT_PIN_LENGTH)
            horizontalSpacing = array.getDimension(
                R.styleable.lockui_keypadHorizontalSpacing,
                context.dimension(R.dimen.default_horizontal_spacing)
            ) as Int
            verticalSpacing = array.getDimension(
                R.styleable.lockui_keypadVerticalSpacing,
                context.dimension(R.dimen.default_vertical_spacing)
            ) as Int
            textColor = array.getColor(
                R.styleable.lockui_keypadTextColor,
                context.color(R.color.text_numberpressed)
            )
            textSize = array.getDimension(
                R.styleable.lockui_keypadTextSize,
                context.dimension(R.dimen.default_text_size)
            ) as Int
            buttonSize = array.getDimension(
                R.styleable.lockui_keypadButtonSize,
                context.dimension(R.dimen.default_button_size)
            ) as Int
            deleteButtonWidthSize = array.getDimension(
                R.styleable.lockui_keypadDeleteButtonSize,
                context.dimension(
                    R.dimen.default_delete_button_size_width
                )
            ) as Int
            deleteButtonHeightSize = array.getDimension(
                R.styleable.lockui_keypadDeleteButtonSize,
                context.dimension(
                    R.dimen.default_delete_button_size_height
                )
            ) as Int

            buttonBackgroundDrawable =
                array.getDrawable(R.styleable.lockui_keypadButtonBackgroundDrawable)
            deleteButtonDrawable =
                array.getDrawable(R.styleable.lockui_keypadDeleteButtonDrawable)
            showDeleteButton =
                array.getBoolean(R.styleable.lockui_keypadShowDeleteButton, true)
            deleteButtonPressedColor = array.getColor(
                R.styleable.lockui_keypadDeleteButtonPressedColor,
                context.color(R.color.text_numberpressed)
            )

        } finally {
            array.recycle()
        }
    }

    private fun updateUi(context: Context) {

    }
}