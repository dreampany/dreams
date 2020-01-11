package com.dreampany.tools.injector.ui

import com.dreampany.tools.ui.activity.LaunchActivity
import com.dreampany.tools.ui.activity.NavigationActivity
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.activity.WebActivity
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
    @ContributesAndroidInjector(modules = [HomeModule::class, MoreModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            SettingsModule::class,
            LicenseModule::class,
            AboutModule::class,
            AppModule::class,
            ScanModule::class,
            NoteModule::class,
            WordModule::class,
            VpnModule::class,
            RadioModule::class,
            CryptoModule::class,
            BlockModule::class,
            ResumeModule::class
        ]
    )
    abstract fun toolsActivity(): ToolsActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun webActivity(): WebActivity
}