package com.dreampany.lca.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.lca.ui.fragment.LibraryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class LibraryModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [FavoritesModule::class, CoinAlertsModule::class])
    abstract fun libraryFragment(): LibraryFragment;
}