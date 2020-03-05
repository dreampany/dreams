package com.dreampany.lockui.ui.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dreampany.common.extensions.gone
import com.dreampany.common.extensions.hash256
import com.dreampany.common.misc.Constants
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.lockui.R
import com.dreampany.lockui.databinding.PinActivityBinding
import com.dreampany.lockui.widget.LockView

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PinActivity : BaseActivity(), LockView.LockListener {

    private lateinit var bind: PinActivityBinding
    private var setPin = false
    private var firstPin = Constants.Default.STRING

    companion object {
        private val PIN_LENGTH = 4
        private val EXTRA_SET_PIN = "set_pin"

        private val RESULT_BACK_PRESSED = RESULT_FIRST_USER

        private val PREFERENCES = "com.dreampany.lockui"
        private val KEY_PIN = "pin"

        fun getIntent(context: Context, setPin: Boolean): Intent {
            val intent =
                Intent(context, PinActivity::class.java)
            intent.putExtra(EXTRA_SET_PIN, setPin)
            return intent
        }
    }

    override fun isFullScreen(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun getLayoutId(): Int = R.layout.pin_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {

    }

    override fun onBackPressed() {
        setResult(RESULT_BACK_PRESSED)
        super.onBackPressed()
    }

    override fun onComplete(pin: String) {
        if (setPin) {
            setPin(pin)
        } else {
            checkPin(pin)
        }
    }

    override fun onEmpty() {

    }

    override fun onPinChange(pinLength: Int, intermediatePin: String) {

    }

    private fun initUi() {
        bind = getBinding<PinActivityBinding>()
        setPin = intent.getBooleanExtra(EXTRA_SET_PIN, false)

        if (setPin) {
            setPinUi()
        } else {
            val pin = getPinFromSharedPreferences()
            if (pin.isEmpty()) {
                setPinUi()
                setPin = true
            } else {
                // TODO Finger Print
            }
        }

        bind.lockView.setDots(bind.dots)
        bind.lockView.setListener(this)
        bind.lockView.setPinLength(PIN_LENGTH)
    }

    private fun setPinUi() {
        bind.attempts.gone()
    }

    private fun setPin(pin: String) {
        if (firstPin.isEmpty()) {
            firstPin = pin
            bind.title.setText(R.string.lockui_second_pin)
            bind.lockView.reset()
        } else {
            if (pin == firstPin) {
                writePinToSharedPreferences(pin)
                setResult(RESULT_OK)
                finish()
            } else {
                shake()
                bind.title.setText(R.string.lockui_try_again)
                bind.lockView.reset()
                firstPin = Constants.Default.STRING
            }
        }
    }

    private fun checkPin(pin: String) {
        if (pin.hash256().equals(getPinFromSharedPreferences())) {
            setResult(RESULT_OK)
            finish()
        } else {
            shake()
            bind.attempts.setText(R.string.lockui_wrong_pin)
            bind.lockView.reset()
        }
    }

    private fun shake() {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
            bind.lockView, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f
        ).setDuration(1000)
        animator.start()
    }

    private fun writePinToSharedPreferences(pin: String) {
        val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PIN, pin.hash256()).apply()
    }

    private fun getPinFromSharedPreferences(): String {
        val prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PIN, Constants.Default.NULL) ?: Constants.Default.STRING
    }
}