package com.dreampany.tools.inject.ui.vm.news

import androidx.lifecycle.ViewModel
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.tools.ui.misc.vm.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class MiscViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearch(vm: SearchViewModel): ViewModel
}