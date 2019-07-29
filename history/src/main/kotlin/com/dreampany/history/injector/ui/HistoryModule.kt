package com.dreampany.history.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.history.ui.fragment.HistoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class HistoryModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun historyFragment(): HistoryFragment
}