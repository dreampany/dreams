package com.dreampany.common.ui.activity

import android.os.Bundle
import android.os.Parcelable
import android.view.Window
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.ui.fragment.BaseInjectorFragment
import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseInjectorActivity : DaggerAppCompatActivity() {

    protected var fireOnStartUi: Boolean = true
    private lateinit var binding: ViewDataBinding
    private lateinit var toolbar: Toolbar
    protected var task: Task<*, *, *>? = null

    protected var fragment: BaseInjectorFragment? = null

    open fun isFullScreen(): Boolean = false

    open fun hasBinding(): Boolean = false

    @LayoutRes
    open fun getLayoutId(): Int = 0

    @IdRes
    open fun getToolbarId(): Int = 0

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

    protected fun <T : ViewDataBinding> getBinding(): T {
        return binding as T
    }

    private fun initLayout(@LayoutRes layoutId: Int) {
        if (isFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        if (hasBinding()) {
            binding = DataBindingUtil.setContentView(this, layoutId)
            binding.setLifecycleOwner(this)
        }
        else {
            setContentView(layoutId)
        }
    }

    private fun initToolbar() {
        val toolbarId = getToolbarId()
        if (toolbarId != 0) {
            toolbar = findViewById<Toolbar>(toolbarId)
            setSupportActionBar(toolbar)
        }
    }

    protected fun getBundle(): Bundle? {
        return intent.extras
    }

    protected fun <T> getIntentValue(key: String, bundle: Bundle?): T? {
        var t: T? = null
        if (bundle != null) {
            t = bundle.getParcelable<Parcelable>(key) as T?
        }
        if (bundle != null && t == null) {
            t = bundle.getSerializable(key) as T?
        }
        return t
    }

    protected fun <T> getIntentValue(key: String): T? {
        val bundle = getBundle()
        return getIntentValue<T>(key, bundle)
    }

    protected fun <T : Task<*, *, *>> getTask(freshTask: Boolean = false): T? {
        if (task == null || freshTask) {
            task = getIntentValue<T>(Constants.Keys.TASK)
        }
        return task as T?
    }
}