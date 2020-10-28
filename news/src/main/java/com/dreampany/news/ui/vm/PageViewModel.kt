package com.dreampany.news.ui.vm

import android.app.Application
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.countryCode
import com.dreampany.framework.misc.exts.title
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.news.app.App
import com.dreampany.news.data.enums.Action
import com.dreampany.news.data.enums.State
import com.dreampany.news.data.enums.Subtype
import com.dreampany.news.data.enums.Type
import com.dreampany.news.data.model.Category
import com.dreampany.news.data.model.Page
import com.dreampany.news.data.source.pref.Prefs
import com.dreampany.news.data.source.repo.CategoryRepo
import com.dreampany.news.data.source.repo.PageRepo
import com.dreampany.news.ui.model.PageItem
import com.dreampany.theme.Colors
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
class PageViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val colors: Colors,
    private val pref: Prefs,
    private val categoryRepo: CategoryRepo,
    private val repo: PageRepo
) : BaseViewModel<Type, Subtype, State, Action, Page, PageItem, UiTask<Type, Subtype, State, Action, Page>>(
    application,
    rm
) {

    fun reads() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Page>? = null
            var errors: SmartError? = null
            try {
                val categories = categoryRepo.reads()
                val regionPage = region
                val categoryPages = categories?.toPages()
                val customPages = repo.reads()

                val total = arrayListOf<Page>()
                total.add(regionPage)
                if (categoryPages != null) {
                    total.addAll(categoryPages)
                }
                if (customPages != null) {
                    total.addAll(customPages)
                }
                result = total
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

    fun readsCache() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Page>? = null
            var errors: SmartError? = null
            try {
                result = pref.pages
                /*if (result.isNullOrEmpty()) {
                    result = pref.categories?.toPages()
                }*/
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

    fun write(query: String) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Page? = null
            var errors: SmartError? = null
            try {
                val page = Page(query)
                page.type = Page.Type.CUSTOM
                page.title = query.title
                val opt = repo.write(page)
                if (opt > 0) {
                    result = page
                    pref.commitPage(result)
                }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem())
            }
        }
    }

    private val region: Page
        get() {
            val regionCode = getApplication<App>().countryCode
            val title = Locale(Constant.Default.STRING, regionCode).displayName
            val page = Page(regionCode)
            page.type = Page.Type.REGION
            page.title = title.title
            return page
        }

    private suspend fun List<Category>.toPages(): List<Page> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val page = Page(input.id)
                page.type = Page.Type.CATEGORY
                page.title = input.title
                page
            }
        }
    }

    private suspend fun List<Page>.toItems(): List<PageItem> {
        val input = this
        val pages = pref.pages /*?: pref.categories?.toPages()*/
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val item = PageItem(input)
                item.color = colors.nextColor(Type.PAGE.name)
                if (!pages.isNullOrEmpty()) {
                    item.select = pages.contains(input)
                }
                item
            }
        }
    }

    private suspend fun Page.toItem(): PageItem {
        val input = this
        val pages = pref.pages
        return withContext(Dispatchers.IO) {
            val item = PageItem(input)
            item.color = colors.nextColor(Type.PAGE.name)
            if (!pages.isNullOrEmpty()) {
                item.select = pages.contains(input)
            }
            item
        }
    }

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            Type.PAGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.PAGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.PAGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<PageItem>?) {
        postMultiple(
            Type.PAGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: PageItem?, state: State = State.DEFAULT) {
        postSingle(
            Type.PAGE,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

}