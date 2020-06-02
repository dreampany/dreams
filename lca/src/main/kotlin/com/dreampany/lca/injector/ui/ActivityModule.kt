package com.dreampany.lca.injector.ui

import com.dreampany.frame.misc.ActivityScope
import com.dreampany.lca.ui.activity.SplashActivity
import com.dreampany.lca.ui.activity.HomeActivity
import com.dreampany.lca.ui.activity.ToolsActivity
import com.dreampany.frame.ui.activity.WebActivity
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
    abstract fun launchActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MoreModule::class, CoinsModule::class, LibraryModule::class, IcoModule::class, NewsModule::class])
    abstract fun navigationActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, LicenseModule::class, AboutModule::class, CoinModule::class, CoinAlertModule::class])
    abstract fun toolsActivity(): ToolsActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun webActivity(): WebActivity

}