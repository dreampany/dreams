package com.dreampany.radio.injector.vm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.dreampany.radio.vm.StationViewModel
import com.dreampany.radio.vm.MoreViewModel
import com.dreampany.frame.vm.ViewModelKey
import com.dreampany.frame.vm.factory.ViewModelFactory
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
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindMoreViewModel(vm: MoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StationViewModel::class)
    abstract fun bindStationViewModel(vm: StationViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}