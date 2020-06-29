package com.dreampany.nearby.ui.publish.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.nearby.data.enums.Action
import com.dreampany.nearby.data.enums.State
import com.dreampany.nearby.data.enums.Subtype
import com.dreampany.nearby.data.enums.Type
import com.dreampany.nearby.data.model.media.Apk
import com.dreampany.nearby.data.source.pref.AppPref
import com.dreampany.nearby.data.source.repo.ApkRepo
import com.dreampany.nearby.ui.publish.model.ApkItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 29/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ApkViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val repo: ApkRepo
) : BaseViewModel<Type, Subtype, State, Action, Apk, ApkItem, UiTask<Type, Subtype, State, Action, Apk>>(
    application,
    rm
) {

    fun loadApks() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Apk>? = null
            var errors: SmartError? = null
            try {
                result = repo.gets()
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

    fun loadApk(id: String) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Apk? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                result = repo.get(id)
                result?.let { favorite = repo.isFavorite(it) }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite))
            }
        }
    }

    fun loadFavoriteApks() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Apk>? = null
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

    fun toggleFavorite(input: Apk) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Apk? = null
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

    private suspend fun List<Apk>.toItems(): List<ApkItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val favorite = repo.isFavorite(input)
                ApkItem(input, favorite)
            }
        }
    }

    private suspend fun Apk.toItem(favorite: Boolean): ApkItem {
        val input = this
        return withContext(Dispatchers.IO) {
            ApkItem(input, favorite = favorite)
        }
    }

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            Type.APK,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.APK,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.APK,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<ApkItem>?) {
        postMultiple(
            Type.APK,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: ApkItem?, state: State = State.DEFAULT) {
        postSingle(
            Type.APK,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}