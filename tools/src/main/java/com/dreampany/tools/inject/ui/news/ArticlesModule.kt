package com.dreampany.tools.inject.ui.news

import com.dreampany.tools.ui.news.fragment.ArticlesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ArticlesModule {
    @ContributesAndroidInjector
    abstract fun articles(): ArticlesFragment
}