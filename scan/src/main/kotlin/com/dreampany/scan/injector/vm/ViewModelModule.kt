package com.dreampany.scan.injector.vm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.dreampany.scan.vm.ScanViewModel
import com.dreampany.frame.injector.ViewModelKey
import com.dreampany.frame.vm.factory.ViewModelFactory
import com.dreampany.scan.vm.FlagViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ScanViewModel::class)
    abstract fun bindScanViewModel(scanViewModel: ScanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlagViewModel::class)
    abstract fun bindFlagViewModel(flagViewModel: FlagViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}