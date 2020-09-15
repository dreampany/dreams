package com.dreampany.framework.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class InjectActivity : BaseActivity(), HasAndroidInjector {

    @Inject
    internal lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    protected var startByInject: Boolean = true
    private lateinit var binding: ViewDataBinding

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        startByBase = false
        super.onCreate(savedInstanceState)
        if (layoutRes != 0) {
            binding = DataBindingUtil.setContentView(this, layoutRes)
            binding.setLifecycleOwner(this)
            initToolbar()
        }
        if (startByInject) {
            onStartUi(savedInstanceState)
            params?.let { app.logEvent(it) }
        }
    }

    protected fun <T : ViewDataBinding> getBinding(): T = binding as T

    protected fun <T : ViewModel> createVm(clazz: KClass<T>): T =
        ViewModelProvider(this, factory).get(clazz.java)
}