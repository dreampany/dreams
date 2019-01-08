package com.dreampany.word.injector.ui

import com.dreampany.frame.misc.ActivityScope
import com.dreampany.vision.injector.ui.LiveTextOcrModule
import com.dreampany.vision.injector.ui.TextOcrModule
import com.dreampany.vision.ui.activity.LivePreviewActivity
import com.dreampany.vision.ui.activity.TextOcrActivity
import com.dreampany.word.ui.activity.LaunchActivity
import com.dreampany.word.ui.activity.LoaderActivity
import com.dreampany.word.ui.activity.NavigationActivity
import com.dreampany.word.ui.activity.ToolsActivity
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
    @ContributesAndroidInjector(modules = [MoreModule::class, RecentModule::class, HomeModule::class, FlagModule::class, SearchModule::class, OcrModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        SettingsModule::class,
        LicenseModule::class,
        AboutModule::class,
        RecentsModule::class,
        WordModule::class,
        LiveTextOcrModule::class])
    abstract fun toolsActivity(): ToolsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [TextOcrModule::class])
    abstract fun textOcrActivity(): TextOcrActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun livePreviewActivity(): LivePreviewActivity
}