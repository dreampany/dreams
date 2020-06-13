package com.dreampany.crypto.inject.ui.activity

import com.dreampany.crypto.inject.ui.home.CoinModule
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.crypto.inject.ui.home.HomeModule
import com.dreampany.crypto.inject.ui.more.MoreModule
import com.dreampany.crypto.inject.ui.news.NewsModule
import com.dreampany.crypto.ui.home.activity.CoinActivity
import com.dreampany.crypto.ui.home.activity.FavoriteCoinsActivity
import com.dreampany.crypto.ui.home.activity.HomeActivity
import com.dreampany.crypto.ui.splash.SplashActivity
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

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, NewsModule::class, MoreModule::class])
    abstract fun home(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [CoinModule::class])
    abstract fun coin(): CoinActivity

    @ContributesAndroidInjector
    abstract fun favorites(): FavoriteCoinsActivity
}