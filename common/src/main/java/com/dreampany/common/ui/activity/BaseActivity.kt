package com.dreampany.common.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.common.misc.func.Executors

import javax.inject.Inject

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseActivity<T> : AppCompatActivity() where T : ViewDataBinding {

    @Inject
    protected lateinit var ex: Executors
    protected lateinit var binding: T

    @get:LayoutRes
    open val layoutRes: Int = 0

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutRes != 0) {
            binding = DataBindingUtil.setContentView(this, layoutRes)
            binding.lifecycleOwner = this
            //initToolbar()
        }
        onStartUi(savedInstanceState)
    }
}