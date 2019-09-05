package com.dreampany.word.injector.ui

import com.dreampany.framework.misc.FragmentScope
import com.dreampany.word.ui.fragment.WordsVisionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 9/27/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class WordsVisionModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun wordsVisionFragment(): WordsVisionFragment
}