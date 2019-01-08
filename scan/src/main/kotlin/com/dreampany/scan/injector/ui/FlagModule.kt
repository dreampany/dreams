package com.dreampany.scan.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.scan.ui.fragment.FlagFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class FlagModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun flagFragment(): FlagFragment;
}