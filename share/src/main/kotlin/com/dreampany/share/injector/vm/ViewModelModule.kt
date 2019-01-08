package com.dreampany.share.injector.vm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.dreampany.frame.vm.ViewModelKey
import com.dreampany.frame.vm.factory.ViewModelFactory
import com.dreampany.share.vm.*
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
    @ViewModelKey(NetworkViewModel::class)
    abstract fun bindNetworkViewModel(vm: NetworkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(vm: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverViewModel::class)
    abstract fun bindDiscoverViewModel(vm: DiscoverViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShareViewModel::class)
    abstract fun bindShareViewModel(vm: ShareViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MediaViewModel::class)
    abstract fun bindMediaViewModel(vm: MediaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApkViewModel::class)
    abstract fun bindApkViewModel(vm: ApkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel::class)
    abstract fun bindImageViewModel(vm: ImageViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}