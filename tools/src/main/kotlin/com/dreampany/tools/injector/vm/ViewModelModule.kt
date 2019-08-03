package com.dreampany.tools.injector.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.tools.vm.DemoViewModel
import com.dreampany.tools.vm.MoreViewModel
import com.dreampany.frame.misc.ViewModelKey
import com.dreampany.frame.vm.factory.ViewModelFactory
import com.dreampany.tools.vm.FeatureViewModel
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
    abstract fun bindDemoViewModel(vm: DemoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeatureViewModel::class)
    abstract fun bindFeatureViewModel(vm: FeatureViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}