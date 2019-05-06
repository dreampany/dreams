package com.dreampany.quran.injector.ui

import com.dreampany.quran.ui.fragment.HomeFragment
import com.dreampany.frame.misc.FragmentScope
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
    @ContributesAndroidInjector(modules = [FirstModule::class, SecondModule::class, ThirdModule::class])
    abstract fun homeFragment(): HomeFragment;
}