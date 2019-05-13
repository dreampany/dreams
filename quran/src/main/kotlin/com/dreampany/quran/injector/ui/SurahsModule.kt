package com.dreampany.quran.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.quran.ui.fragment.SurahsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class SurahsModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun surahsFragment(): SurahsFragment
}
