package com.dreampany.crypto.inject.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.crypto.ui.home.vm.CoinViewModel
import com.dreampany.crypto.ui.home.vm.ExchangeViewModel
import com.dreampany.crypto.ui.home.vm.TickerViewModel
import com.dreampany.crypto.ui.home.vm.TradeViewModel
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.framework.ui.vm.factory.ViewModelFactory
import com.dreampany.crypto.ui.more.vm.MoreViewModel
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
}