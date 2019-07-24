package com.dreampany.history.injector.ui

import com.dreampany.history.ui.fragment.LicenseFragment
import com.dreampany.frame.misc.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class LicenseModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun licenseFragment(): LicenseFragment;
}