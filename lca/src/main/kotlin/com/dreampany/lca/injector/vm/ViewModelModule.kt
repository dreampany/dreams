package com.dreampany.lca.injector.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.misc.ViewModelKey
import com.dreampany.frame.vm.factory.ViewModelFactory
import com.dreampany.lca.vm.*
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
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    fun bindMoreViewModel(vm: MoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoinViewModel::class)
    fun bindCoinViewModel(vm: CoinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoinsViewModel::class)
    fun bindCoinsViewModel(vm: CoinsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoinAlertViewModel::class)
    fun bindCoinAlertViewModel(vm: CoinAlertViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    fun bindFlagViewModel(vm: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeViewModel::class)
    fun bindExchangeViewModel(vm: ExchangeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MarketViewModel::class)
    fun bindMarketViewModel(vm: MarketViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GraphViewModel::class)
    fun bindGraphViewModel(vm: GraphViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LiveIcoViewModel::class)
    fun bindLiveIcoViewModel(vm: LiveIcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingIcoViewModel::class)
    fun bindUpcomingIcoViewModel(vm: UpcomingIcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FinishedIcoViewModel::class)
    fun bindFinishedIcoViewModel(vm: FinishedIcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    fun bindNewsViewModel(vm: NewsViewModel): ViewModel

    @Singleton
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}