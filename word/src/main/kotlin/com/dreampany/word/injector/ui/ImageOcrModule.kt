package com.dreampany.word.injector.ui

import com.dreampany.framework.misc.FragmentScope
import com.dreampany.word.ui.fragment.ImageOcrFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class ImageOcrModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun imageOcrFragment(): ImageOcrFragment
}