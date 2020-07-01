package com.dreampany.tube.ui.home.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.pref.AppPref
import com.dreampany.tube.data.source.repo.VideoRepo
import com.dreampany.tube.ui.home.model.CategoryItem
import com.dreampany.tube.ui.home.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by roman on 1/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val repo: VideoRepo
) : BaseViewModel<Type, Subtype, State, Action, Video, VideoItem, UiTask<Type, Subtype, State, Action, Video>>(
    application,
    rm
) {

/*    fun loadCategories() {
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
    }*/

    /*private suspend fun List<Category>.toItems(): List<CategoryItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val favorite = repo.isFavorite(input)
                CategoryItem(input, favorite)
            }
        }
    }*/

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<VideoItem>?) {
        postMultiple(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: VideoItem?, state: State = State.DEFAULT) {
        postSingle(
            Type.VIDEO,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}