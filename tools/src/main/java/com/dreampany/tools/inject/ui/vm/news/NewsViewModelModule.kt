package com.dreampany.tools.inject.ui.vm.news

import androidx.lifecycle.ViewModel
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.tools.ui.news.vm.ArticleViewModel
import com.dreampany.tools.ui.news.vm.PageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class NewsViewModelModule {

/*    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategory(vm: CategoryViewModel): ViewModel*/

    @Binds
    @IntoMap
    @ViewModelKey(PageViewModel::class)
    abstract fun bindPage(vm: PageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    abstract fun bindArticle(vm: ArticleViewModel): ViewModel
}