package com.dreampany.tube.ui.home.vm

import android.app.Application
import com.dreampany.framework.misc.exts.countryCode
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.theme.Colors
import com.dreampany.tube.app.App
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.source.pref.AppPref
import com.dreampany.tube.data.source.repo.CategoryRepo
import com.dreampany.tube.ui.home.model.CategoryItem
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
class CategoryViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val colors: Colors,
    private val pref: AppPref,
    private val repo: CategoryRepo
) : BaseViewModel<Type, Subtype, State, Action, Category, CategoryItem, UiTask<Type, Subtype, State, Action, Category>>(
    application,
    rm
) {

    fun loadCategories() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Category>? = null
            var errors: SmartError? = null
            try {
                var regionCode = getApplication<App>().countryCode
                result = repo.gets(regionCode)
                if (result.isNullOrEmpty()) {
                    regionCode = Locale.US.country
                    result = repo.gets(regionCode)
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

    fun loadCategoriesOfCache() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Category>? = null
            var errors: SmartError? = null
            try {
                result = pref.categories
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

    private suspend fun List<Category>.toItems(): List<CategoryItem> {
        val input = this
        val categories = pref.categories
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val favorite = repo.isFavorite(input)
                val item = CategoryItem(input, favorite)
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