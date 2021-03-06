package com.dreampany.lca.injector.ui

import com.dreampany.lca.ui.fragment.LiveIcoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class LiveIcoModule {
    @ContributesAndroidInjector
    abstract fun liveIcoFragment(): LiveIcoFragment;
}
