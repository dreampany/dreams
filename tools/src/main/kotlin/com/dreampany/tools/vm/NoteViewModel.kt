package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.enums.ApkType
import com.dreampany.tools.data.enums.NoteType
import com.dreampany.tools.data.misc.ApkMapper
import com.dreampany.tools.data.misc.ApkRequest
import com.dreampany.tools.data.misc.NoteMapper
import com.dreampany.tools.data.misc.NoteRequest
import com.dreampany.tools.data.model.Apk
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.ApkRepository
import com.dreampany.tools.data.source.repository.NoteRepository
import com.dreampany.tools.ui.model.ApkItem
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.tools.ui.model.UiTask
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteViewModel @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val stateMapper: StateMapper,
    private val stateRepo: StateRepository,
    private val mapper: NoteMapper,
    private val repo: NoteRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Note, NoteItem, UiTask<Note>>(application, rx, ex, rm) {

    fun request(request: NoteRequest) {
        if (request.action == Action.ADD || request.action == Action.UPDATE) {
            addSingle(request)
            return
        }
        if (request.action == Action.GET) {
            if (request.single) {
                loadSingle(request)
            } else {
                loadMultiple(request)
            }
            return
        }

    }

    private fun addSingle(request: NoteRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain(saveUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResult(Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun loadSingle(request: NoteRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(loadUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResult(Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun loadMultiple(request: NoteRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(loadUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResult(Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun saveUiItemRx(request: NoteRequest): Maybe<NoteItem> {
        return saveItemRx(request).flatMap { getUiItemRx(it) }
    }

    private fun loadUiItemRx(request: NoteRequest): Maybe<NoteItem> {
        return getItemRx(request).flatMap { getUiItemRx(it) }
    }

    private fun loadUiItemsRx(request: NoteRequest): Maybe<List<NoteItem>> {
        return repo.getItemsRx().flatMap { getUiItemsRx(it) }
    }

    private fun saveItemRx(request: NoteRequest): Maybe<Note> {
        return Maybe.create { emitter ->
            val note = mapper.toItem(request.id, request.title, request.description)
            if (note == null) {
                emitter.onError(EmptyException())
            } else {
                repo.putItem(note)
                emitter.onSuccess(note)
            }
        }
    }

    private fun getItemRx(request: NoteRequest): Maybe<Note> {
        return repo.getItemRx(request.id!!)
    }

    private fun getItemsRx(request: NoteRequest): Maybe<List<Note>> {
        return Maybe.create { emitter ->

        }
    }

    private fun getUiItem(item: Note): NoteItem {
        return NoteItem.getItem(item)
    }

    private fun getUiItemRx(item: Note): Maybe<NoteItem> {
        return Maybe.create { emitter ->
            emitter.onSuccess(getUiItem(item))
        }
    }

    private fun getUiItemsRx(items: List<Note>): Maybe<List<NoteItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }
}