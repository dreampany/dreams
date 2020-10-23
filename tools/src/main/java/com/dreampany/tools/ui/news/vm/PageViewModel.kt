package com.dreampany.tools.ui.news.vm

import android.app.Application
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.countryCode
import com.dreampany.framework.misc.exts.toTitle
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.theme.Colors
import com.dreampany.tools.app.App
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.news.Category
import com.dreampany.tools.data.model.news.Page
import com.dreampany.tools.data.source.news.pref.NewsPref
import com.dreampany.tools.data.source.news.repo.CategoryRepo
import com.dreampany.tools.data.source.news.repo.PageRepo
import com.dreampany.tools.ui.news.model.PageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 22/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val colors: Colors,
    private val pref: NewsPref,
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
                var categories = categoryRepo.gets()
                if (!categories.isNullOrEmpty()) {
                    val total = ArrayList(categories)
                    total.add(0, regionCategory())
                    categories = total
                }

                val categoryPages = categories?.toPages()
                val customPages = repo.reads()
                val total = arrayListOf<Page>()
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

    fun readCache() {
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
                page.title = query.toTitle()
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

    fun backupPages() {
        uiScope.launch {
            try {
                val pages = pref.categories?.toPages()
                if (pages != null) {
                    pref.commitPages(pages)
                    pref.commitPagesSelection()
                }
            } catch (error: SmartError) {
                Timber.e(error)
            }
        }
    }

    private fun regionCategory(): Category {
        val regionCode = getApplication<App>().countryCode
        val name = Locale(Constant.Default.STRING, regionCode).displayName
        val category = Category(regionCode)
        category.title = name
        category.type = Category.Type.REGION
        return category
    }

    private fun liveCategory(): Category {
        val category = Category(Category.Type.LIVE.value)
        category.title = Category.Type.LIVE.value.toTitle()
        category.type = Category.Type.LIVE
        return category
    }

    private fun upcomingCategory(): Category {
        val category = Category(Category.Type.UPCOMING.value)
        category.title = Category.Type.UPCOMING.value.toTitle()
        category.type = Category.Type.UPCOMING
        return category
    }

    private suspend fun List<Category>.toPages(): List<Page> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val page = Page(input.id)
                page.type = input.type.toPageType()
                page.title = input.title
                page
            }
        }
    }

    private suspend fun Category.Type.toPageType(): Page.Type {
        when (this) {
            Category.Type.REGION -> return Page.Type.REGION
            Category.Type.LIVE -> return Page.Type.EVENT
            Category.Type.UPCOMING -> return Page.Type.EVENT
        }
        return Page.Type.DEFAULT
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
        return withContext(kotlinx.coroutines.Dispatchers.IO) {
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