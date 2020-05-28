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

    private lateinit var binding: ViewDataBinding

    override fun androidInjector(): AndroidInjector<Any> = injector

    //override val hasBinding: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        createdByChild = true
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.setLifecycleOwner(this)
        initToolbar()
        onStartUi(savedInstanceState)
    }

    protected fun <T : ViewDataBinding> getBinding(): T = binding as T

    protected fun <T : ViewModel> createVm(clazz: KClass<T>): T =
        ViewModelProvider(this, factory).get(clazz.java)
}