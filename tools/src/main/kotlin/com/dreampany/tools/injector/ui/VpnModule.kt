package com.dreampany.tools.injector.ui

import com.dreampany.framework.injector.annote.FragmentScope
import com.dreampany.tools.ui.fragment.vpn.CountriesFragment
import com.dreampany.tools.ui.fragment.vpn.FavoriteServersFragment
import com.dreampany.tools.ui.fragment.vpn.ServersFragment
import com.dreampany.tools.ui.fragment.vpn.VpnHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class VpnModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): VpnHomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun serversFragment(): ServersFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun countriesFragment(): CountriesFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun favoriteFragment(): FavoriteServersFragment
}