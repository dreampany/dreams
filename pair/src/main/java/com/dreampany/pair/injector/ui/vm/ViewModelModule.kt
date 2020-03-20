package com.dreampany.pair.injector.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.inject.annote.ViewModelKey
import com.dreampany.common.ui.vm.factory.ViewModelFactory
import com.dreampany.pair.ui.auth.vm.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ViewModelModule {
    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindQuestionViewModel(vm: AuthViewModel): ViewModel
}