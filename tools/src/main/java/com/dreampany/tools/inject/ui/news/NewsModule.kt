package com.dreampany.tools.inject.ui.news

import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.tools.ui.news.activity.NewsActivity
import com.dreampany.tools.ui.news.activity.PagesActivity
import com.dreampany.tools.ui.news.activity.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 14/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class NewsModule {
    @ContributesAndroidInjector
    abstract fun pages(): PagesActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ArticlesModule::class])
    abstract fun news(): NewsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class])
    abstract fun settings(): SettingsActivity
}