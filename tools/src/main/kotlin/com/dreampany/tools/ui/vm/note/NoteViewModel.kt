package com.dreampany.tools.ui.vm.note

import android.app.Application
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.NoteMapper
import com.dreampany.tools.ui.misc.NoteRequest
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.NoteRepository
import com.dreampany.tools.ui.model.NoteItem
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NoteViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: NoteMapper,
    private val repo: NoteRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Note, NoteItem, UiTask<Note>>(application, rx, ex, rm), NetworkManager.Callback {

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: NoteRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestSingle(request: NoteRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }
        val disposable = rx
                .backToMain(requestUiItemRx(request))
                .doOnSubscribe { subscription ->
                    if (request.progress) {
                        postProgress(state = request.state, action = request.action, loading =true)
                    }
                }
                .subscribe({ result ->
                    if (request.progress) {
                        postProgress(state = request.state, action = request.action, loading =false)
                    }
                    postResult(state = request.state,
                        action = request.action,
                        data = result)
                }, { error ->
                    if (request.progress) {
                        postProgress(state = request.state, action = request.action, loading =false)
                    }
                    postFailures(state = request.state,
                        action = request.action,
                        error = MultiException(error, ExtraException()))
                })
        addSingleSubscription(disposable)
    }

    private fun requestMultiple(request: NoteRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
                .backToMain(requestUiItemsRx(request))
                .doOnSubscribe { subscription ->
                    if (request.progress) {
                        postProgress(state = request.state, action = request.action, loading =true)
                    }
                }
                .subscribe({ result ->
                    if (request.progress) {
                        postProgress(state = request.state, action = request.action, loading =false)
                    }
                    postResult(state = request.state,
                        action = request.action,
                        data = result)
                }, { error ->
                    if (request.progress) {
                        postProgress(state = request.state, action = request.action, loading =false)
                    }
                    postFailures(state = request.state,
                        action = request.action,
                        error = MultiException(error, ExtraException()))
                })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemRx(request: NoteRequest): Maybe<NoteItem> {
        when (request.action) {
            Action.ADD-> {
                return addItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.EDIT -> {
                return editItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.FAVORITE -> {
                return favoriteItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.ARCHIVE -> {
                return archiveItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.TRASH -> {
                return trashItemRx(request).flatMap { getUiItemRx(request, it) }
            }
            Action.DELETE -> {
                return deleteItemRx(request).flatMap { getUiItemRx(request, it) }
            }
        }
        return getItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun requestUiItemsRx(request: NoteRequest): Maybe<List<NoteItem>> {
        if (request.action == Action.FAVORITE) {
            return storeRepo
                    .getItemsRx(Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
                    .flatMap { getUiItemsOfStoresRx(request, it) }
        }
        return getItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun addItemRx(request: NoteRequest): Maybe<Note> {
        return Maybe.create { emitter ->
            val note = mapper.getItem(request.id, request.title, request.description)
            if (note == null) {
                emitter.onError(EmptyException())
            } else {
                repo.putItem(note)
                emitter.onSuccess(note)
            }
        }
    }

    private fun editItemRx(request: NoteRequest): Maybe<Note> {
        return Maybe.create { emitter ->
            val note = mapper.getItem(request.id, request.title, request.description)
            if (note == null) {
                emitter.onError(EmptyException())
            } else {
                repo.putItem(note)
                emitter.onSuccess(note)
            }
        }
    }

    private fun favoriteItemRx(request: NoteRequest): Maybe<Note> {
        return Maybe.create { emitter ->
            request.input?.run {
                val toggle = toggleFavorite(request.id!!)
                emitter.onSuccess(this)
                return@create
            }
            emitter.onError(EmptyException())
        }
    }

    private fun archiveItemRx(request: NoteRequest): Maybe<Note> {
        return Maybe.create { emitter ->
            val note = request.input
            if (note == null) {
                emitter.onError(EmptyException())
            } else {
                putStore(request.id!!, Type.NOTE, Subtype.DEFAULT, State.ARCHIVED)
                removeStore(request.id!!, Type.NOTE, Subtype.DEFAULT, State.TRASH)
                emitter.onSuccess(note)
            }
        }
    }

    private fun trashItemRx(request: NoteRequest): Maybe<Note> {
        return Maybe.create { emitter ->
            val note = request.input
            if (note == null) {
                emitter.onError(EmptyException())
            } else {
                putStore(request.id!!, Type.NOTE, Subtype.DEFAULT, State.TRASH)
                removeStore(request.id!!, Type.NOTE, Subtype.DEFAULT, State.ARCHIVED)
                emitter.onSuccess(note)
            }
        }
    }

    private fun deleteItemRx(request: NoteRequest): Maybe<Note> {
        return Maybe.create { emitter ->
            val note = request.input
            if (note == null) {
                emitter.onError(EmptyException())
            } else {
                val result = repo.delete(note)
                removeStore(request.id!!, Type.NOTE, Subtype.DEFAULT, State.TRASH)
                removeStore(request.id!!, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
                removeStore(request.id!!, Type.NOTE, Subtype.DEFAULT, State.ARCHIVED)
                emitter.onSuccess(note)
            }
        }
    }

    private fun getItemRx(request: NoteRequest): Maybe<Note> {
        return repo.getItemRx(request.id!!)
    }

    private fun getItemsRx(request: NoteRequest): Maybe<List<Note>> {
        return repo.getItemsRx();
    }

    private fun getUiItem(request: NoteRequest, item: Note): NoteItem {
        var uiItem: NoteItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = NoteItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        adjustFavorite(item, uiItem)
        return uiItem
    }

    private fun getUiItem(request: NoteRequest, store: Store): NoteItem {
        val note = mapper.getItem(store, repo)
        return getUiItem(request, note!!)
    }

    private fun getUiItemRx(request: NoteRequest, item: Note): Maybe<NoteItem> {
        return Maybe.create { emitter ->
/*            if (request.action == Action.FAVORITE) {
                toggleFavorite(item.id)
            }*/
            val uiItem = getUiItem(request, item)
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItemsRx(request: NoteRequest, items: List<Note>): Maybe<List<NoteItem>> {
        return Flowable.fromIterable(items)
                .map { getUiItem(request, it) }
                .toList()
                .toMaybe()
    }

    private fun getUiItemsOfStoresRx(request: NoteRequest, items: List<Store>): Maybe<List<NoteItem>> {
        return Flowable.fromIterable(items)
                .map { getUiItem(request, it) }
                .toList()
                .toMaybe()
    }

    private fun adjustFavorite(item: Note, uiItem: NoteItem) {
        uiItem.favorite = isFavorite(item)
    }

    private fun isFavorite(item: Note): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(item.id)) {
            val favorite = hasStore(item.id, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
            Timber.v("Favorite of %s %s", item.id, favorite)
            favorites.put(item.id, favorite)
        }
        return favorites.get(item.id)
    }

    private fun toggleFavorite(id: String): Boolean {
        val favorite = hasStore(id, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
        if (favorite) {
            removeStore(id, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, false)
        } else {
            putStore(id, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, true)
        }
        return favorites.get(id)
    }

    private fun hasStore(id: String, type: Type, subtype: Subtype, state: State): Boolean {
        return storeRepo.isExists(id, type, subtype, state)
    }

    private fun putStore(id: String, type: Type, subtype: Subtype, state: State): Long {
        val store = storeMapper.getItem(id, type, subtype, state)
        return storeRepo.putItem(store)
    }

    private fun removeStore(id: String, type: Type, subtype: Subtype, state: State): Int {
        val store = storeMapper.getItem(id, type, subtype, state)
        return storeRepo.delete(store)
    }
}