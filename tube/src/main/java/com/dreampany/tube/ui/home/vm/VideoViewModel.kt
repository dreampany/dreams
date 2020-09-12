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
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.pref.AppPref
import com.dreampany.tube.data.source.repo.CategoryRepo
import com.dreampany.tube.data.source.repo.VideoRepo
import com.dreampany.tube.misc.AppConstants
import com.dreampany.tube.ui.home.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.Query
import timber.log.Timber
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
    private val categoryRepo: CategoryRepo,
    private val repo: VideoRepo
) : BaseViewModel<Type, Subtype, State, Action, Video, VideoItem, UiTask<Type, Subtype, State, Action, Video>>(
    application,
    rm
) {


    fun loadRegionVideos(regionCode: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfRegionCode(regionCode, offset, AppConstants.Limits.VIDEOS)
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

    fun loadEventVideos(eventType: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfEvent(eventType, offset, AppConstants.Limits.VIDEOS)
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

    fun loadVideos(categoryId: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfCategoryId(categoryId, offset, AppConstants.Limits.VIDEOS)
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
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfQuery(query, 0, AppConstants.Limits.VIDEOS)
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

    fun loadFavoriteVideos() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getFavorites()
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

    fun toggleFavorite(input: Video) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Video? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                favorite = repo.toggleFavorite(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), state = State.FAVORITE)
            }
        }
    }

    /*fun loadVideosOfRegionCode(regionCode : String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {

                result = repo.getsOfRegionCode(regionCode, offset, AppConstants.Limits.VIDEOS)
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

    private suspend fun List<Video>.toItems(): List<VideoItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val favorite = repo.isFavorite(input)
                VideoItem(input, favorite)
            }
        }
    }

    private suspend fun Video.toItem(favorite: Boolean): VideoItem {
        val input = this
        return withContext(Dispatchers.IO) {
            VideoItem(input, favorite)
        }
    }

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