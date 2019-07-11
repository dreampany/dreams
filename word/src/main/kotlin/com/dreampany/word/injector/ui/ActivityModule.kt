package com.dreampany.word.injector.ui

import com.dreampany.frame.misc.ActivityScope
import com.dreampany.word.ui.activity.*
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
    @ContributesAndroidInjector
    abstract fun loaderActivity(): LoaderActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MoreModule::class, HomeModule::class, FavoritesModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, LicenseModule::class, AboutModule::class, WordModule::class])
    abstract fun toolsActivity(): ToolsActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun webActivity(): WebActivity
}