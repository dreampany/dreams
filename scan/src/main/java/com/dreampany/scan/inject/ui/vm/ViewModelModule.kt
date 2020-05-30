package com.dreampany.scan.inject.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.framework.ui.vm.factory.ViewModelFactory
import com.dreampany.scan.ui.camera.vm.ScanViewModel
import com.dreampany.scan.ui.more.vm.MoreViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindMore(vm: MoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanViewModel::class)
    abstract fun bindScan(vm: ScanViewModel): ViewModel
}