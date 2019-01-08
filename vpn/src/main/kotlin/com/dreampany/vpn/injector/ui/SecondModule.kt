package com.dreampany.vpn.injector.ui

import com.dreampany.vpn.ui.fragment.SecondFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class SecondModule {
    @ContributesAndroidInjector
    abstract fun secondFragment(): SecondFragment;
}