package com.dreampany.lca.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.lca.ui.fragment.MoreFragment
import com.dreampany.lca.ui.fragment.NewsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class NewsModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun newsFragment(): NewsFragment
}