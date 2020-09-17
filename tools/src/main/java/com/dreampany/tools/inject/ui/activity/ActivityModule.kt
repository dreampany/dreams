package com.dreampany.tools.inject.ui.activity

import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.tools.inject.ui.crypto.CryptoModule
import com.dreampany.tools.inject.ui.history.HistoryModule
import com.dreampany.tools.inject.ui.home.HomeModule
import com.dreampany.tools.inject.ui.more.MoreModule
import com.dreampany.tools.inject.ui.news.NewsModule
import com.dreampany.tools.inject.ui.note.NoteModule
import com.dreampany.tools.inject.ui.radio.RadioModule
import com.dreampany.tools.inject.ui.settings.SettingsModule
import com.dreampany.tools.inject.ui.wifi.WifiModule
import com.dreampany.tools.ui.home.activity.HomeActivity
import com.dreampany.tools.ui.splash.SplashActivity
import com.dreampany.tools.ui.web.WebActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        CryptoModule::class,
        RadioModule::class,
        NoteModule::class,
        HistoryModule::class,
        WifiModule::class,
        NewsModule::class
    ]
)
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, MoreModule::class, SettingsModule::class])
    abstract fun home(): HomeActivity

    @ContributesAndroidInjector
    abstract fun web(): WebActivity
}