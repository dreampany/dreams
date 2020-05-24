package com.dreampany.tools.inject.ui.vm.wifi

import androidx.lifecycle.ViewModel
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.tools.ui.wifi.vm.WifiViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by roman on 25/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class WifiViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(WifiViewModel::class)
    abstract fun bindWifi(vm: WifiViewModel): ViewModel
}