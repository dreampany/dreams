package com.dreampany.vision.injector.ui

import com.dreampany.framework.injector.annote.FragmentScope
import com.dreampany.vision.ui.fragment.TextOcrFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 9/27/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class TextOcrModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun textOcrFragment(): TextOcrFragment
}