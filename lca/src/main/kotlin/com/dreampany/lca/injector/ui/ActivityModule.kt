package com.dreampany.lca.injector.ui

import com.dreampany.frame.misc.ActivityScope
import com.dreampany.lca.ui.activity.LaunchActivity
import com.dreampany.lca.ui.activity.NavigationActivity
import com.dreampany.lca.ui.activity.ToolsActivity
import com.dreampany.lca.ui.activity.WebActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/23/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun launchActivity(): LaunchActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LiveCoinsModule::class, FlagCoinsModule::class, IcoModule::class, NewsModule::class, MoreModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [CoinModule::class, SettingsModule::class, LicenseModule::class, AboutModule::class])
    abstract fun toolsActivity(): ToolsActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun webActivity(): WebActivity

}