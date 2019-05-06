package com.dreampany.quran.injector.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.quran.vm.DemoViewModel
import com.dreampany.quran.vm.MoreViewModel
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
    @ViewModelKey(DemoViewModel::class)
    abstract fun bindDemoViewModel(demoViewModel: DemoViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}