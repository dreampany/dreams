package com.dreampany.lockui.newui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.dreampany.common.misc.extension.bindInflater
import com.dreampany.common.misc.extension.loadAnim
import com.dreampany.lockui.R
import com.dreampany.lockui.databinding.ViewPinBinding

/**
 * Created by roman on 7/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PinView : LinearLayoutCompat {

    private lateinit var bind: ViewPinBinding

    private lateinit var pinOne: View
    private lateinit var pinTwo: View
    private lateinit var pinThree: View
    private lateinit var pinFour: View
    private lateinit var prompt: TextView

    private lateinit var pins: Array<View>


    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initUi(context)
    }

    fun addPin() {
        for (index in pins.indices) {
            if (pins[index].isSelected.not()) {
                pins[index].isSelected = true
                pins[index].loadAnim(R.anim.pin_enter)
                break
            }
        }
    }

    fun removePin() {
        for (index in pins.indices.reversed()) {
            if (pins[index].isSelected) {
                pins[index].isSelected = false
                pins[index].loadAnim(R.anim.pin_remove)
                break
            }
        }
    }

    fun reset() {
        pins.forEach {
            it.isSelected = false
            it.isEnabled = true
        }
    }

    fun error() {
        pins.forEach {
            it.isEnabled = false
        }
        bind.pinView.loadAnim(R.anim.pin_error)
    }

    private fun initUi(context: Context) {
        bind = context.bindInflater(R.layout.view_pin, parent = this)
        pins = arrayOf(bind.pinOne, bind.pinTwo, bind.pinThree, bind.pinFour)
    }


}