package com.dreampany.word.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.word.ui.fragment.FavoritesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class FavoritesModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun favoritesFragment(): FavoritesFragment
}