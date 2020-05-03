package com.dreampany.lockui.newui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import com.dreampany.framework.misc.extension.bindInflater
import com.dreampany.lockui.R
import com.dreampany.lockui.databinding.ViewPasswordBinding

/**
 * Created by roman on 8/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PasswordView : LinearLayoutCompat {

    interface Callback {
        fun onNumKey()
        fun onBackKey()
        fun onDeleteKey()
        fun onPinCode(code: String)
    }

    private var callback: Callback? = null

    private lateinit var bind: ViewPasswordBinding

    private var index: Int = -1
    private lateinit var selected: IntArray

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
        reset()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun reset() {
        index = -1
        selected = IntArray(4)
    }

    private fun initUi(context: Context) {
        bind = context.bindInflater(R.layout.view_password, parent = this)

        bind.key0.setOnClickListener { keyPressed(0) }
        bind.key1.setOnClickListener { keyPressed(1) }
        bind.key2.setOnClickListener { keyPressed(2) }
        bind.key3.setOnClickListener { keyPressed(3) }
        bind.key4.setOnClickListener { keyPressed(4) }
        bind.key5.setOnClickListener { keyPressed(5) }
        bind.key6.setOnClickListener { keyPressed(6) }
        bind.key7.setOnClickListener { keyPressed(7) }
        bind.key8.setOnClickListener { keyPressed(8) }
        bind.key9.setOnClickListener { keyPressed(9) }

        bind.keyBack.setOnClickListener { backKey() }
        bind.keyDelete.setOnClickListener { deleteKey() }
    }

    private fun keyPressed(key: Int) {
        index++
        callback?.onNumKey()
        if (index < 4)
            selected[index] = key
        if (index == 3)
            callback?.onPinCode(code())
    }

    private fun backKey() {
        callback?.onBackKey()
    }

    private fun deleteKey() {
        callback?.onDeleteKey()
        if (index > 3) index = 3
        if (index < 0) index = 0
        index--
    }

    private fun code(): String {
        val code = StringBuilder()
        selected.forEach {
            code.append(it)
        }
        return code.toString()
    }
}