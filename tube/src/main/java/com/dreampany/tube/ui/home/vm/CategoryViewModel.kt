package com.dreampany.tube.ui.home.vm

import android.app.Application
import com.dreampany.framework.misc.constant.Constants
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
import com.dreampany.tube.data.source.mapper.CategoryMapper
import com.dreampany.tube.data.source.pref.AppPref
import com.dreampany.tube.data.source.repo.CategoryRepo
import com.dreampany.tube.ui.home.model.CategoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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
    private val mapper : CategoryMapper,
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
                var countryCode = getApplication<App>().countryCode
                result = repo.gets(countryCode)
                if (result.isNullOrEmpty()) {
                    countryCode = Locale.US.country
                    result = repo.gets(countryCode)
                }
                if (!result.isNullOrEmpty()) {
                    val total = ArrayList(result)
                    total.add(0, regionCategory())
                    total.add(1, liveCategory())
                    total.add(2, upcomingCategory())
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

    private fun regionCategory() : Category {
        val regionCode = getApplication<App>().countryCode
        val name = Locale(Constants.Default.STRING, regionCode).displayName
        val category = Category(regionCode)
        category.title = name
        category.type = CategoryType.REGION
        return category
    }

    private fun liveCategory() : Category {
        val category = Category(CategoryType.LIVE.value)
        category.title = CategoryType.LIVE.value.toTitle()
        category.type = CategoryType.LIVE
        return category
    }

    private fun upcomingCategory() : Category {
        val category = Category(CategoryType.UPCOMING.value)
        category.title = CategoryType.UPCOMING.value.toTitle()
        category.type = CategoryType.UPCOMING
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

    private fun categoryType(id : String) : CategoryType {
        if (mapper.isIsoCountry(id)) {
            return CategoryType.REGION
        }
        return CategoryType.DEFAULT
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