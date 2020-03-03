package com.dreampany.common.ui.activity

import android.os.Bundle
import android.view.Window
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ViewDataBinding
    private lateinit var toolbar: Toolbar
    protected var fireOnStartUi: Boolean = true

    open fun hasBinding(): Boolean {
        return false
    }

    open fun isFullScreen(): Boolean {
        return false
    }

    @LayoutRes
    open fun getLayoutId(): Int {
        return 0
    }

    @IdRes
    open fun getToolbarId(): Int {
        return 0
    }

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun initLayout(layoutId: Int) {
        if (isFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        if (hasBinding())
            binding = DataBindingUtil.setContentView(this, layoutId)
        else
            setContentView(layoutId)
    }

    private fun initToolbar() {
        toolbar = findViewById<Toolbar>(getToolbarId())
        setSupportActionBar(toolbar)
    }
}