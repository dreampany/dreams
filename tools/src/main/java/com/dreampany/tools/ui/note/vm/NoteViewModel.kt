package com.dreampany.tools.ui.note.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.note.NoteAction
import com.dreampany.tools.data.enums.note.NoteState
import com.dreampany.tools.data.enums.note.NoteSubtype
import com.dreampany.tools.data.enums.note.NoteType
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.pref.NotePref
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
    private val pref: NotePref,
    private val repo: NoteRepo
) : BaseViewModel<NoteType, NoteSubtype, NoteState, NoteAction, Note, NoteItem, UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note>>(
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
                postResult(result?.toItem(favorite), NoteState.LOADED)
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
                postResult(result?.toItem(favorite), state = NoteState.FAVORITE)
            }
        }
    }

    fun saveNote(id: String?, title: String, description: String?) {
        uiScope.launch {
            postProgressSingle(true)
            val state = if (id.isNullOrEmpty()) NoteState.ADDED else NoteState.EDITED
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
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<NoteItem>?) {
        postMultiple(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: NoteItem?, state: NoteState = NoteState.DEFAULT) {
        postSingle(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            state,
            NoteAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}