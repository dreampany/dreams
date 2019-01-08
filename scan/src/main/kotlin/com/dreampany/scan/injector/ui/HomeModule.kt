package com.dreampany.scan.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.scan.ui.fragment.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class HomeModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment;
}