package com.dreampany.tools.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.tools.ui.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class WordModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): WordHomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun wordFragment(): WordFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun favoriteWordsFragment(): FavoriteWordsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun wordQuizFragment(): WordQuizFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun visionFragment(): WordVisionFragment
}