package com.dreampany.tools.inject.ui.crypto

import com.dreampany.tools.ui.crypto.fragment.GraphFragment
import com.dreampany.tools.ui.crypto.fragment.DetailsFragment
import com.dreampany.tools.ui.crypto.fragment.MarketFragment
import com.dreampany.tools.ui.crypto.fragment.TickersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class CoinModule {
    @ContributesAndroidInjector
    abstract fun details(): DetailsFragment

    @ContributesAndroidInjector
    abstract fun market(): MarketFragment

    @ContributesAndroidInjector
    abstract fun tickers(): TickersFragment

    @ContributesAndroidInjector
    abstract fun graph(): GraphFragment
}