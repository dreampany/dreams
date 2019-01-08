package com.dreampany.share.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.share.ui.fragment.DiscoverFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class DiscoverModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun discoverFragment(): DiscoverFragment
}