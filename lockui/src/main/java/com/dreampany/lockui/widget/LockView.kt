package com.dreampany.lockui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.extensions.color
import com.dreampany.common.extensions.dimension
import com.dreampany.common.misc.Constants
import com.dreampany.common.ui.misc.ItemSpaceDecoration
import com.dreampany.lockui.R
import com.dreampany.lockui.ui.adapter.LockAdapter
import com.dreampany.lockui.ui.model.Delete
import com.dreampany.lockui.ui.model.Item
import com.dreampany.lockui.ui.model.Number

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LockView : RecyclerView,
    com.dreampany.common.ui.adapter.BaseAdapter.OnItemClickListener<Item> {

    interface LockListener {
        fun onComplete(pin: String)
        fun onEmpty()
        fun onPinChange(pinLength: Int, intermediatePin: String)
    }

    private val SPAN_COUNT = 3
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

    private var dots: Dots? = null
    private lateinit var adapter: LockAdapter

    private var listener: LockListener? = null

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

    override fun onItemClick(item: Item) {
        if (item is Number) {
            onNumberClick(item)
        } else if (item is Delete) {
            onDeleteClick(item)
        }
    }

    override fun onChildItemClick(view: View, item: Item) {

    }

    fun setListener(listener: LockListener) {
        this.listener = listener
    }

    fun reset() {
        pin = Constants.Default.STRING
        adapter.setPinLength(pin.length)
        adapter.notifyItemChanged(adapter.itemCount - 1)

        dots?.updateDot(pin.length)
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
        layoutManager = GridLayoutManager(context, SPAN_COUNT)

        adapter = LockAdapter(this)
        setAdapter(adapter)
        addItemDecoration(
            ItemSpaceDecoration(
                horizontalSpacing,
                verticalSpacing,
                SPAN_COUNT,
                false
            )
        )
        setOverScrollMode(OVER_SCROLL_NEVER)
    }

    private fun onNumberClick(number: Number) {
        if (pin.length < pinLength) {
            pin = pin.plus(number.number.toString())
            dots?.updateDot(pin.length)

            if (pin.length == 1) {
                adapter.setPinLength(pin.length)
                adapter.notifyItemChanged(adapter.itemCount - 1)
            }

            if (pin.length == pinLength)
                listener?.onComplete(pin)
            else
                listener?.onPinChange(pin.length, pin)
        } else {
            if (showDeleteButton.not()) {
                reset()
                pin = pin.plus(number.number.toString())
                dots?.updateDot(pin.length)
                listener?.onPinChange(pin.length, pin)
            } else {
                listener?.onComplete(pin)
            }
        }
    }

    private fun onDeleteClick(delete: Delete) {
        if (pin.isNotEmpty()) {
            pin = pin.substring(0, pin.length - 1)
            dots?.updateDot(pin.length)

            if (pin.isEmpty()) {
                adapter.setPinLength(pin.length)
                adapter.notifyItemChanged(adapter.itemCount - 1)
            }

            if (pin.isEmpty()) {
                listener?.onEmpty()
                pin = Constants.Default.STRING
            } else
                listener?.onPinChange(pin.length, pin)
        } else {
            listener?.onEmpty()
        }
    }
}