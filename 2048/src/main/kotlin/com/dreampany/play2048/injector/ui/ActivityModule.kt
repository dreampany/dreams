package com.dreampany.play2048.injector.ui

import com.dreampany.frame.misc.ActivityScope
import com.dreampany.play2048.ui.activity.HomeActivity
import com.dreampany.play2048.ui.activity.ToolsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/23/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class ActivityModule {
/*    @ActivityScope
    @ContributesAndroidInjector
    abstract fun launchActivity(): LaunchActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, MoreModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, LicenseModule::class, AboutModule::class])
    abstract fun toolsActivity(): ToolsActivity*/

    @ActivityScope
    @ContributesAndroidInjector(modules = [PlayModule::class, MoreModule::class])
    abstract fun homeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, LicenseModule::class, AboutModule::class])
    abstract fun toolsActivity(): ToolsActivity
}