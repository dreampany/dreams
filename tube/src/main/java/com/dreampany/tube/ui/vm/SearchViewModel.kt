package com.dreampany.tube.ui.vm

import android.app.Application
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.id
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Search
import com.dreampany.tube.data.source.repo.SearchRepo
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.nl.languageid.LanguageIdentification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SearchViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val repo: SearchRepo
) : BaseViewModel<Type, Subtype, State, Action, Search, Search, UiTask<Type, Subtype, State, Action, Search>>(
    application,
    rm
) {
    fun write(input: String) {
        uiScope.launch {
            progressSingle(true)
            var result: Search? = null
            var errors: SmartError? = null
            try {
                val ref = "tools"
                val search = Search(input.id)
                if (!search.hasFirestore()) {
                    search.keyword = input
                    search.language = input.language()
                    search.hits = mapOf(ref to 0L)
                    val success = repo.write(search)
                    if (success > 0) {
                        search.trackFirestore()
                    }
                }
                repo.hit(input.id, ref)
                result = search
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result)
            }
        }
    }

    fun hit(input: Search) {
        uiScope.launch {
            progressSingle(true)
            var result: Search? = null
            var errors: SmartError? = null
            try {
                val opt = repo.write(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result)
            }
        }
    }

    private suspend fun String.language(): String {
        val input = this
        return withContext(Dispatchers.IO) {
            val detector = LanguageIdentification.getClient()
            val task: Task<String> = detector.identifyLanguage(input)
            val result = Tasks.await(task)
            result
        }

    }

    private suspend fun Search.hasFirestore(): Boolean {
        val exists = storeRepo.isExists(
            id,
            Type.SEARCH.value,
            Subtype.DEFAULT.value,
            State.DEFAULT.value
        )
        return exists
    }

    private suspend fun Search.trackFirestore(): Long {
        val store = storeMapper.readStore(
            id,
            Type.SEARCH.value,
            Subtype.DEFAULT.value,
            State.DEFAULT.value
        )
        if (store != null) {
            return storeRepo.write(store)
        }
        return -1
    }

    private fun progressSingle(progress: Boolean) {
        postProgressSingle(
            Type.SEARCH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError) {
        postMultiple(
            Type.SEARCH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: Search?, state: State = State.DEFAULT) {
        postSingle(
            Type.SEARCH,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}