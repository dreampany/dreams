package com.dreampany.crypto.ui.news

import android.app.Application
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Article
import com.dreampany.crypto.data.source.repo.ArticleRepo
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
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
) : BaseViewModel<Type, Subtype, State, Action, Article, ArticleItem, UiTask<Type, Subtype, State, Action, Article>>(
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