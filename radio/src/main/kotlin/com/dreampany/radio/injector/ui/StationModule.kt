package com.dreampany.radio.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.radio.ui.fragment.StationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class StationModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun stationFragment(): StationFragment;
}