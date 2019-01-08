package com.dreampany.play2048.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.play2048.ui.fragment.PlayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class PlayModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun playFragment(): PlayFragment;
}