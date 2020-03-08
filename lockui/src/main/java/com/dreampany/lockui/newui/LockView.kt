package com.dreampany.lockui.newui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.dreampany.common.misc.extension.bindInflater
import com.dreampany.common.misc.extension.visible
import com.dreampany.lockui.R
import com.dreampany.lockui.databinding.ViewLockBinding

/**
 * Created by roman on 7/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LockView : RelativeLayout, PasswordView.Callback {

    interface Callback {
        fun onPinCode(code: String)
        fun onCorrect()
        fun onBackPressed()
    }

    private var callback: LockView.Callback? = null

    private lateinit var bind: ViewLockBinding

    private var code: String? = null
    private var title: String? = null

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
        initUi(context)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_BACK,
            KeyEvent.KEYCODE_HOME -> callback?.onBackPressed()
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onNumKey() {
        bind.pinView.addPin()
    }

    override fun onPinCode(code: String) {
        Handler().postDelayed({ checkCode(code) }, 200)
    }

    override fun onDeleteKey() {
        bind.pinView.removePin()
    }

    override fun onBackKey() {
        callback?.onBackPressed()
    }

    fun setCallback(callback: LockView.Callback) {
        this.callback = callback
    }

    fun setCode(code: String) {
        this.code = code
    }

    fun setTitle(title: String?) {
        this.title = title
        bind.prompt.text = title
        bind.prompt.visible()
    }

    fun setIcon(drawable: Drawable?) {
        bind.icon.setImageDrawable(drawable)
    }

    fun reset() {
        bind.passwordView.reset()
        bind.pinView.reset()
    }

    private fun initUi(context: Context) {
        bind = context.bindInflater(R.layout.view_lock, parent = this)
        bind.passwordView.setCallback(this)
        bind.pinView.reset()
        bind.prompt.text = title
    }

    private fun checkCode(code: String) {
        Handler().postDelayed({ callback?.onPinCode(code) }, 200)
        if (this.code.isNullOrEmpty()) {
            if (this.code.equals(code)) {
                Handler().postDelayed({ callback?.onCorrect() }, 200)
            } else {
                bind.pinView.error()
            }
        }
        Handler().postDelayed({ reset() }, 800)
    }
}