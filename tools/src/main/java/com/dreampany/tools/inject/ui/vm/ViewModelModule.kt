package com.dreampany.tools.inject.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.framework.ui.vm.factory.ViewModelFactory
import com.dreampany.tools.inject.ui.vm.crypto.CryptoViewModelModule
import com.dreampany.tools.inject.ui.vm.history.HistoryViewModelModule
import com.dreampany.tools.inject.ui.vm.note.NoteViewModelModule
import com.dreampany.tools.inject.ui.vm.radio.RadioViewModelModule
import com.dreampany.tools.inject.ui.vm.wifi.WifiViewModelModule
import com.dreampany.tools.ui.home.vm.FeatureViewModel
import com.dreampany.tools.ui.more.vm.MoreViewModel
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
@Module(
    includes = [
        CryptoViewModelModule::class,
        RadioViewModelModule::class,
        NoteViewModelModule::class,
        HistoryViewModelModule::class,
        WifiViewModelModule::class
    ]
)
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeatureViewModel::class)
    abstract fun bindFeature(vm: FeatureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindMore(vm: MoreViewModel): ViewModel
}