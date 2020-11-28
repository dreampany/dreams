package com.dreampany.tube.inject.ui.activity

import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.tube.inject.ui.home.HomeModule
import com.dreampany.tube.inject.ui.search.SearchModule
import com.dreampany.tube.inject.ui.settings.SettingsModule
import com.dreampany.tube.ui.home.activity.FavoriteVideosActivity
import com.dreampany.tube.ui.home.activity.HomeActivity
import com.dreampany.tube.ui.player.VideoPlayerActivity
import com.dreampany.tube.ui.settings.activity.CategoriesActivity
import com.dreampany.tube.ui.settings.activity.PagesActivity
import com.dreampany.tube.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ContributesAndroidInjector
    abstract fun categories(): CategoriesActivity

    @ContributesAndroidInjector
    abstract fun pages(): PagesActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, SearchModule::class, SettingsModule::class])
    abstract fun home(): HomeActivity

    @ContributesAndroidInjector
    abstract fun player(): VideoPlayerActivity

    @ContributesAndroidInjector
    abstract fun favorites(): FavoriteVideosActivity
}