package com.dreampany.radio.ui.vm

import android.app.Application
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.countryCode
import com.dreampany.framework.misc.exts.title
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.radio.app.App
import com.dreampany.radio.data.enums.Action
import com.dreampany.radio.data.enums.State
import com.dreampany.radio.data.enums.Subtype
import com.dreampany.radio.data.enums.Type
import com.dreampany.radio.data.model.Page
import com.dreampany.radio.data.source.pref.Prefs
import com.dreampany.radio.data.source.repo.PageRepo
import com.dreampany.radio.ui.model.PageItem
import com.dreampany.theme.Colors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 2/11/20
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
                val regionPage = region
                val basicPages = basicPages
                val customPages = repo.reads()

                val total = arrayListOf<Page>()
                total.add(regionPage)
                total.addAll(basicPages)
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
                    pref.writePage(result)
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
            page.type = Page.Type.LOCAL
            page.title = title.title
            return page
        }

    private val basicPages: List<Page>
        get() {
            val pages = arrayListOf<Page>()
            pages.add(Page(Page.Type.TREND))
            pages.add(Page(Page.Type.POPULAR))
            pages.add(Page(Page.Type.RECENT))
            pages.add(Page(Page.Type.CHANGE))
            return pages
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