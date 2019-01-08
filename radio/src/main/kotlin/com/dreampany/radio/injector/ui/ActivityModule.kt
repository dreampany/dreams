package com.dreampany.radio.injector.ui

import com.dreampany.frame.misc.ActivityScope
import com.dreampany.radio.ui.activity.HomeActivity
import com.dreampany.radio.ui.activity.ToolsActivity
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
    @ContributesAndroidInjector(modules = [StationModule::class, MoreModule::class])
    abstract fun homeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, LicenseModule::class, AboutModule::class])
    abstract fun toolsActivity(): ToolsActivity
}