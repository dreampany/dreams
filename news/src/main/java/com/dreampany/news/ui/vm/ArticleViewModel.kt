package com.dreampany.news.ui.vm

import android.app.Application
import com.dreampany.framework.misc.exts.countryCode
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.news.app.App
import com.dreampany.news.data.enums.Action
import com.dreampany.news.data.enums.State
import com.dreampany.news.data.enums.Subtype
import com.dreampany.news.data.enums.Type
import com.dreampany.news.data.model.Article
import com.dreampany.news.data.model.Page
import com.dreampany.news.data.source.repo.ArticleRepo
import com.dreampany.news.ui.model.ArticleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 28/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ArticleViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: ArticleRepo
) : BaseViewModel<Type, Subtype, State, Action, Article, ArticleItem, UiTask<Type, Subtype, State, Action, Article>>(
    application,
    rm
) {

    fun loadLocalArticles(countryCode: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Article>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsByCountry(countryCode, 1, 100)
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

    fun loadSearch(query: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Article>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfQuery(query, "en", 1, 100)
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
            Type.ARTICLE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.ARTICLE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<ArticleItem>?) {
        postMultiple(
            Type.ARTICLE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}