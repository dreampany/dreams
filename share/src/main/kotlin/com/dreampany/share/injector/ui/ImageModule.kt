package com.dreampany.share.injector.ui

import com.dreampany.share.ui.fragment.ImageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class ImageModule {
    @ContributesAndroidInjector
    abstract fun imageFragment(): ImageFragment;
}