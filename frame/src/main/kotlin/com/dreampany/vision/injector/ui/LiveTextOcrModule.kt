package com.dreampany.vision.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.vision.ui.fragment.LiveTextOcrFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 9/27/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class LiveTextOcrModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun liveTextOcrFragment(): LiveTextOcrFragment
}