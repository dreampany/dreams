package com.dreampany.share.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.share.ui.fragment.MediaFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class MediaModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [ApkModule::class, ImageModule::class])
    abstract fun mediaFragment(): MediaFragment
}