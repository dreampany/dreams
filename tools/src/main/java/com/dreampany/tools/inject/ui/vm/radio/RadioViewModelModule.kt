package com.dreampany.tools.inject.ui.vm.radio

import androidx.lifecycle.ViewModel
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.tools.ui.radio.vm.StationViewModel
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
abstract class RadioViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StationViewModel::class)
    abstract fun bindStation(vm: StationViewModel): ViewModel
}