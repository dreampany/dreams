package com.dreampany.share.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.share.ui.fragment.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class SettingsModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment;
}