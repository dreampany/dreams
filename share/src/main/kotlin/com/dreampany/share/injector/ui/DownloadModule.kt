package com.dreampany.share.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.share.ui.fragment.DownloadFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class DownloadModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun firstFragment(): DownloadFragment;
}