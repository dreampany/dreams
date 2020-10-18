package com.dreampany.tube.inject.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.framework.ui.vm.factory.ViewModelFactory
import com.dreampany.tube.ui.vm.CategoryViewModel
import com.dreampany.tube.ui.vm.VideoViewModel
import com.dreampany.tube.ui.more.vm.MoreViewModel
import com.dreampany.tube.ui.vm.PageViewModel
import com.dreampany.tube.ui.vm.TimeViewModel
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
    @ViewModelKey(TimeViewModel::class)
    abstract fun bindTime(vm: TimeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategory(vm: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PageViewModel::class)
    abstract fun bindPage(vm: PageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoViewModel::class)
    abstract fun bindVideo(vm: VideoViewModel): ViewModel
}