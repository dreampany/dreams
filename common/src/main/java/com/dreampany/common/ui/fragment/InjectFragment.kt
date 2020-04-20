package com.dreampany.common.ui.fragment

import android.content.Context
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

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    protected fun <T : ViewModel> createVm(clazz: KClass<T>): T =
        ViewModelProvider(this, factory).get(clazz.java)

}