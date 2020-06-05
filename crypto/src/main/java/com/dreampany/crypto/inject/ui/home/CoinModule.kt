package com.dreampany.crypto.inject.ui.home

import com.dreampany.crypto.ui.home.fragment.InfoFragment
import com.dreampany.crypto.ui.home.fragment.MarketFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 5/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class CoinModule {
    @ContributesAndroidInjector
    abstract fun info(): InfoFragment

    @ContributesAndroidInjector
    abstract fun market(): MarketFragment
}