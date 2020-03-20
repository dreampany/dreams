package com.dreampany.common.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.ui.fragment.factory.InjectFragmentFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class InjectHostFragment : NavHostFragment() {

    @Inject
    protected lateinit var factory: InjectFragmentFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = factory
        super.onCreate(savedInstanceState)
    }
}