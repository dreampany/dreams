package com.dreampany.tools.inject.ui.vm.crypto

import androidx.lifecycle.ViewModel
import com.dreampany.common.inject.annote.ViewModelKey
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
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
abstract class CryptoViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CoinViewModel::class)
    abstract fun bindCoin(vm: CoinViewModel): ViewModel
}