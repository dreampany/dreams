package com.dreampany.lockui.newui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.dreampany.framework.ui.activity.BaseActivity
import com.dreampany.lockui.R
import com.dreampany.lockui.databinding.LockActivityBinding

/**
 * Created by roman on 8/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LockActivity : BaseActivity(), PasswordView.Callback {

    private lateinit var bind: LockActivityBinding

    private var code: String? = null
    private var title: String? = null

    companion object {
        private val EXTRA_SET_PIN = "set_pin"
        private val EXTRA_PIN = "pin"

        fun getIntent(context: Context, setPin: Boolean): Intent {
            val intent = Intent(context, LockActivity::class.java)
            intent.putExtra(EXTRA_SET_PIN, setPin)
            return intent
        }
    }

    override fun isFullScreen(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun getLayoutId(): Int = R.layout.lock_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onBackPressed() {
        //setResult(PinActivity.RESULT_BACK_PRESSED)
        super.onBackPressed()
    }

    override fun onNumKey() {
        bind.viewLock.pinView.addPin()
    }

    override fun onBackKey() {
        finish()
    }

    override fun onDeleteKey() {
        bind.viewLock.pinView.removePin()
    }

    override fun onPinCode(code: String) {
        Handler().postDelayed({ checkCode(code) }, 200)
    }

    private fun initUi() {
        bind = getBinding<LockActivityBinding>()
        bind.viewLock.passwordView.setCallback(this)
        bind.viewLock.pinView.reset()
        bind.viewLock.prompt.text = title
    }

    private fun checkCode(code: String) {
/*        Handler().postDelayed({
            callback?.onPinCode(code)
        }, 200)*/
        if (this.code.isNullOrEmpty()) {
            if (this.code.equals(code)) {
/*                Handler().postDelayed({
                    callback?.onCorrect()
                }, 200)*/
            } else {
                bind.viewLock.pinView.error()
            }
        }
        Handler().postDelayed({ reset() }, 800)
    }

    fun reset() {
        bind.viewLock.passwordView.reset()
        bind.viewLock.pinView.reset()
    }
}