package com.dreampany.framework.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class InjectFragment : BaseFragment(), HasAndroidInjector {

    @Inject
    internal lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ViewDataBinding

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (prefLayoutRes != 0) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
        if (currentView != null) {
            currentView?.parent?.let { (it as ViewGroup).removeView(currentView) }
            return currentView
        }
        if (layoutRes != 0) {
            binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
            binding.setLifecycleOwner(this)
            currentView = binding.root
        }
        return currentView
    }

    protected fun <T : ViewDataBinding> getBinding(): T = binding as T

    protected fun <T : ViewModel> createVm(clazz: KClass<T>): T =
        ViewModelProvider(this, factory).get(clazz.java)

}