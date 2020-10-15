package com.dreampany.tools.ui.news.vm

import android.app.Application
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.countryCode
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
import com.dreampany.tools.data.model.misc.Category
import com.dreampany.tools.data.source.news.mapper.CategoryMapper
import com.dreampany.tools.data.source.news.pref.NewsPref
import com.dreampany.tools.data.source.news.repo.CategoryRepo
import com.dreampany.tools.ui.news.model.CategoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CategoryViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val colors: Colors,
    private val pref: NewsPref,
    private val mapper : CategoryMapper,
    private val repo: CategoryRepo
)  : BaseViewModel<Type, Subtype, State, Action, Category, CategoryItem, UiTask<Type, Subtype, State, Action, Category>>(
    application,
    rm
) {

    fun loadCategories() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Category>? = null
            var errors: SmartError? = null
            try {
                result = repo.gets()
                if (!result.isNullOrEmpty()) {
                    val total = ArrayList(result)
                    total.add(0, regionCategory())
                    result = total
                }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                val items = result?.toItems()
                postResult(items)
            }
        }
    }

    private fun regionCategory() : Category {
        val regionCode = getApplication<App>().countryCode
        val name = Locale(Constant.Default.STRING, regionCode).displayName
        val category = Category(regionCode)
        category.category = name
        return category
    }

    private suspend fun List<Category>.toItems(): List<CategoryItem> {
        val input = this
        val categories = pref.categories
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val item = CategoryItem(input)
                item.color = colors.nextColor(Type.CATEGORY.name)
                if (!categories.isNullOrEmpty()) {
                    item.select = categories.contains(input)
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
            Type.CATEGORY,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<CategoryItem>?) {
        postMultiple(
            Type.CATEGORY,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: CategoryItem?, state: State = State.DEFAULT) {
        postSingle(
            Type.CATEGORY,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}