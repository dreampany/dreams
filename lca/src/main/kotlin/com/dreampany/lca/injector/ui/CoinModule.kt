package com.dreampany.lca.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.lca.ui.fragment.CoinFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class CoinModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [DetailsModule::class, MarketModule::class, GraphModule::class])
    abstract fun coinFragment(): CoinFragment;
}