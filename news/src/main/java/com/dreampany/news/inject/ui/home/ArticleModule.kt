package com.dreampany.news.inject.ui.home

import com.dreampany.news.ui.fragment.ArticlesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by $USER on ${DATE}
 * Copyright (c) $YEAR bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ArticleModule {
    @ContributesAndroidInjector
    abstract fun articles(): ArticlesFragment
}