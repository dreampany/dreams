package com.dreampany.lca.injector.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.vm.ViewModelKey
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
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindMoreViewModel(vm: MoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoinViewModel::class)
    abstract fun bindCoinViewModel(vm: CoinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LiveViewModel::class)
    abstract fun bindLiveViewModel(vm: LiveViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FlagViewModel::class)
    abstract fun bindFlagViewModel(vm: FlagViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeViewModel::class)
    abstract fun bindExchangeViewModel(vm: ExchangeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MarketViewModel::class)
    abstract fun bindMarketViewModel(vm: MarketViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GraphViewModel::class)
    abstract fun bindGraphViewModel(vm: GraphViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LiveIcoViewModel::class)
    abstract fun bindLiveICOViewModel(vm: LiveIcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingIcoViewModel::class)
    abstract fun bindUpcomingICOViewModel(vm: UpcomingIcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FinishedIcoViewModel::class)
    abstract fun bindFinishedICOViewModel(vm: FinishedIcoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(vm: NewsViewModel): ViewModel

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}