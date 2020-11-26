package com.dreampany.tools.ui.note.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.repo.NoteRepo
import com.dreampany.tools.data.source.note.room.mapper.NoteMapper
import com.dreampany.tools.ui.note.model.NoteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val mapper: NoteMapper,
    private val repo: NoteRepo
) : BaseViewModel<Type, Subtype, State, Action, Note, NoteItem, UiTask<Type, Subtype, State, Action, Note>>(
    application,
    rm
) {

    fun loadNotes() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Note>? = null
            var errors: SmartError? = null
            try {
                result = repo.getNotes()
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

    fun loadNote(id: String) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Note? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                result = repo.getNote(id)
                result?.let { favorite = repo.isFavorite(it) }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), State.LOADED)
            }
        }
    }

    fun loadFavoriteNotes() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Note>? = null
            var errors: SmartError? = null
            try {
                result = repo.getFavoriteNotes()
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

    fun toggleFavorite(input: Note) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Note? = null
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

    fun saveNote(id: String?, title: String, description: String?) {
        uiScope.launch {
            postProgressSingle(true)
            val state = if (id.isNullOrEmpty()) State.ADDED else State.EDITED
            var result: Note? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                val input = mapper.getItem(id, title, description)
                input?.let {
                    val commitResult = repo.insertItem(it)
                    if (commitResult > 0L) {
                        result = it
                    }
                }
                result?.let { favorite = repo.toggleFavorite(it) }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), state)
            }
        }
    }

    private suspend fun List<Note>.toItems(): List<NoteItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val favorite = repo.isFavorite(input)
                NoteItem.getItem(input, favorite)
            }
        }
    }

    private suspend fun Note.toItem(favorite: Boolean): NoteItem {
        val input = this
        return withContext(Dispatchers.IO) {
            NoteItem.getItem(input, favorite = favorite)
        }
    }

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            Type.NOTE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.NOTE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.NOTE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<NoteItem>?) {
        postMultiple(
            Type.NOTE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: NoteItem?, state: State = State.DEFAULT) {
        postSingle(
            Type.NOTE,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}