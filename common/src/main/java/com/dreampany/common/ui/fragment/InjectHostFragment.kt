package com.dreampany.common.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.dreampany.common.ui.fragment.factory.InjectFragmentFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class InjectHostFragment : NavHostFragment(), HasAndroidInjector {

    @Inject
    internal lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    internal lateinit var factory: InjectFragmentFactory

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = factory
        super.onCreate(savedInstanceState)
    }
}