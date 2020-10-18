package com.dreampany.tube.ui.vm

import android.app.Application
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.countryCode
import com.dreampany.framework.misc.exts.toTitle
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.theme.Colors
import com.dreampany.tube.app.App
import com.dreampany.tube.data.enums.*
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.model.Page
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.data.source.repo.CategoryRepo
import com.dreampany.tube.data.source.repo.PageRepo
import com.dreampany.tube.ui.model.CategoryItem
import com.dreampany.tube.ui.model.PageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 1/7/20
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
                var countryCode = getApplication<App>().countryCode
                var categories = categoryRepo.reads(countryCode)
                if (categories.isNullOrEmpty()) {
                    countryCode = Locale.US.country
                    categories = categoryRepo.reads(countryCode)
                }
                if (!categories.isNullOrEmpty()) {
                    val total = ArrayList(categories)
                    total.add(0, regionCategory())
                    total.add(1, liveCategory())
                    total.add(2, upcomingCategory())
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
                page.type = Page.Type.CATEGORY
                page.title = input.title
                page
            }
        }
    }

    private suspend fun List<Page>.toItems(): List<PageItem> {
        val input = this
        val pages = pref.pages
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

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            Type.CATEGORY,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.CATEGORY,
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