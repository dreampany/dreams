package com.dreampany.tools.inject.ui.vm.crypto

import androidx.lifecycle.ViewModel
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.tools.ui.crypto.vm.*
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

    @Binds
    @IntoMap
    @ViewModelKey(TradeViewModel::class)
    abstract fun bindTrade(vm: TradeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeViewModel::class)
    abstract fun bindExchange(vm: ExchangeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TickerViewModel::class)
    abstract fun bindTicker(vm: TickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GraphViewModel::class)
    abstract fun bindGraph(vm: GraphViewModel): ViewModel
}