package com.dreampany.tools.ui.news.vm

import android.app.Application
import com.dreampany.framework.misc.exts.countryCode
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.app.App
import com.dreampany.tools.data.enums.news.*
import com.dreampany.tools.data.model.news.Category
import com.dreampany.tools.data.model.news.Article
import com.dreampany.tools.data.model.news.Page
import com.dreampany.tools.data.source.news.repo.ArticleRepo
import com.dreampany.tools.ui.news.model.ArticleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticleViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: ArticleRepo
) : BaseViewModel<NewsType, NewsSubtype, NewsState, NewsAction, Article, ArticleItem, UiTask<NewsType, NewsSubtype, NewsState, NewsAction, Article>>(
    application,
    rm
) {

    fun loadArticles() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Article>? = null
            var errors: SmartError? = null
            try {
                val query = "crypto AND currency OR (bitcoin OR ethereum OR litecoin)"
                result = repo.gets(query, "en", 1, 100)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadRegionArticles(regionCode: String,) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Article>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsByCountry(getApplication<App>().countryCode, 1, 100)
                if (result.isNullOrEmpty()) {
                    result = repo.getsByCountry(Locale.US.country, 1, 100)
                }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadArticles(input: Page) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Article>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsByCategory(input.id, 1, 100)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    private suspend fun List<Article>.toItems(): List<ArticleItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                ArticleItem.getItem(input)
            }
        }
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            NewsType.ARTICLE,
            NewsSubtype.DEFAULT,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            NewsType.ARTICLE,
            NewsSubtype.DEFAULT,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<ArticleItem>?) {
        postMultiple(
            NewsType.ARTICLE,
            NewsSubtype.DEFAULT,
            NewsState.DEFAULT,
            NewsAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}