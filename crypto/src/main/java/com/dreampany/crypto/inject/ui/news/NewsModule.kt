package com.dreampany.crypto.inject.ui.news

import com.dreampany.crypto.ui.news.NewsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class NewsModule {
    @ContributesAndroidInjector
    abstract fun news(): NewsFragment
}