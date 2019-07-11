package com.dreampany.translate.injector.ui

import com.dreampany.translate.ui.fragment.FirstFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class FirstModule {
    @ContributesAndroidInjector
    abstract fun firstFragment(): FirstFragment;
}