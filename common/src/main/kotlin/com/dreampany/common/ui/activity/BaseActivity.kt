package com.dreampany.common.ui.activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.common.app.BaseApp
import com.dreampany.common.misc.AppExecutors
import com.dreampany.common.util.AndroidUtil

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseActivity: AppCompatActivity() {

    protected lateinit var ex: AppExecutors
    protected lateinit var binding: ViewDataBinding
    protected var fireOnStartUi: Boolean = true

    open fun getLayoutId(): Int {
        return 0
    }

    open fun getToolbarId(): Int {
        return 0
    }

    open fun isFullScreen(): Boolean {
        return false
    }

    open fun isHomeUp(): Boolean {
        return true
    }

    open fun isScreenOn(): Boolean {
        return false
    }

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AndroidUtil.hasLollipop()) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }
        super.onCreate(savedInstanceState)
        if (isScreenOn()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val layoutId = getLayoutId()
        if (layoutId != 0) {
            initLayout(layoutId)
            initToolbar()
        }
        if (fireOnStartUi) {
            onStartUi(savedInstanceState)
        }
    }

    override fun onDestroy() {
        onStopUi()
        super.onDestroy()
    }

    fun isAlive(): Boolean {
        return AndroidUtil.isAlive(this)
    }

    fun getApp(): BaseApp {
        return application as BaseApp
    }

    private fun initLayout(layoutId: Int) {
        if (isFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(getToolbarId())
        if (toolbar != null) {
            if (isFullScreen()) {
                if (toolbar.isShown) {
                    toolbar.visibility = View.GONE
                }
            } else {
                if (!toolbar.isShown) {
                    toolbar.visibility = View.VISIBLE
                }
                setSupportActionBar(toolbar)
                if (isHomeUp()) {
                    val actionBar = supportActionBar
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true)
                        actionBar.setHomeButtonEnabled(true)
                    }
                }
            }
        }
    }
}