package com.dreampany.tools.ui.vm.word

import android.app.Application
import androidx.fragment.app.Fragment
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.injector.annote.Favorite
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.language.Language
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.WordMapper
import com.dreampany.tools.ui.request.WordRequest
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.word.WordItem
import com.dreampany.tools.util.Util
import com.dreampany.translation.data.source.repository.TranslationRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-08-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val wordPref: WordPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: WordMapper,
    private val repo: WordRepository,
    private val translationRepo: TranslationRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Word, WordItem, UiTask<Word>>(application, rx, ex, rm), NetworkManager.Callback {

    private lateinit var uiCallback: SmartAdapter.Callback<WordItem>

    init {
        network.observe(this, checkInternet = true)
        val language = pref.getLanguage(Language.ENGLISH)
        if (!language.equals(Language.ENGLISH)) {
            //translationRepo.ready(language.code)
        }
    }

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun setUiCallback(callback: SmartAdapter.Callback<WordItem>) {
        this.uiCallback = callback
    }

    fun request(request: WordRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            if (request.suggests) {
                requestMultipleOfString(request)
            } else {
                requestMultiple(request)
            }
        }
    }

    fun isValid(word: String): Boolean {
        if (word.length < 2) return false
        return repo.isValid(word)
    }

    fun share(fragment: Fragment) {
        val word = task!!.input
        val subject = word!!.id
        val text = Util.getText(word)
        AndroidUtil.share(fragment, subject, text)
    }

    private fun requestSingle(request: WordRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postResult(state = request.state, action = request.action, data = result)
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postFailures(
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addSingleSubscription(disposable)
    }

    private fun requestMultiple(request: WordRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postResult(state = request.state, action = request.action, data = result)
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postFailures(
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addMultipleSubscription(disposable)
    }

    private fun requestMultipleOfString(request: WordRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestItemsOfStringRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(
                        state = request.state, action = request.action, loading = true
                    )
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(
                        state = request.state,
                        action = request.action, loading = false
                    )
                }
                postResultOfString(
                    state = request.state,
                    action = request.action, data = result
                )
            }, { error ->
                if (request.progress) {
                    postProgress(
                        state = request.state,
                        action = request.action, loading = false
                    )
                }
                postFailures(
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemRx(request: WordRequest): Maybe<WordItem> {
        return requestItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun requestUiItemsRx(request: WordRequest): Maybe<List<WordItem>> {
        if (request.action == Action.SEARCH) {
            //return storeRepo.getItemsRx(Type.WORD, Subtype.DEFAULT, State.FAVORITE).flatMap {  }
        }
        if (request.action == Action.FAVORITE) {
            return storeRepo
                .getItemsRx(Type.WORD, Subtype.DEFAULT, State.FAVORITE)
                .flatMap { getUiItemsOfStoresRx(request, it) }
        }
        if (request.state == State.HISTORY) {
            return storeRepo
                .getItemsRx(request.type, request.subtype, request.state, request.limit)
                .flatMap { getUiItemsOfStoresRx(request, it) }
        }
        return repo.getItemsRx().flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemsOfStringRx(request: WordRequest): Maybe<List<String>> {
        return repo.getRawWordsRx()
    }

    private fun requestItemRx(request: WordRequest): Maybe<Word> {
        if (request.recent) {
            return wordPref.getRecentWordRx()
        }
        if (request.action == Action.SEARCH) {
            return repo.getItemRx(request.id!!)
        }
        return repo.getItemRx(request.id!!)
    }

    private fun getUiItemRx(request: WordRequest, item: Word): Maybe<WordItem> {
        //Timber.v("Word %s", item.id)
        return Maybe.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            if (request.history) {
                if (item.hasWeight()) {
                    wordPref.setRecentWord(item)
                    val result = putStore(item.id, request.type, request.subtype, State.HISTORY)
                    Constants.Cache.Word.HISTORY = true
                    Timber.v("Word [%s - %d] keep as history", item.id, result)
                }
            }
            if (request.action == Action.FAVORITE) {
                toggleFavorite(item.id)
            }
            val uiItem = getUiItem(request, item)
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItemsRx(request: WordRequest, items: List<Word>): Maybe<List<WordItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItemsOfStoresRx(
        request: WordRequest,
        items: List<Store>
    ): Maybe<List<WordItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: WordRequest, item: Word): WordItem {
        var uiItem: WordItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = WordItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        adjustFavorite(item, uiItem)
        adjustTranslate(request, uiItem)
        return uiItem
    }

    private fun getUiItem(request: WordRequest, store: Store): WordItem {
        val word = mapper.getItem(store, repo)
        val item = getUiItem(request, word!!)
        item.time = store.time
        return item
    }

    private fun adjustFavorite(word: Word, item: WordItem) {
        item.favorite = isFavorite(word)
    }

    private fun adjustTranslate(request: WordRequest, item: WordItem) {
        var translation: String? = null
        if (request.translate) {
            if (item.hasTranslation(request.targetLang)) {
                translation = item.getTranslationBy(request.targetLang)
            } else {
                val textTranslation = translationRepo.getItem(
                    request.sourceLang!!,
                    request.targetLang!!,
                    item.item.id
                )
                textTranslation?.let {
                    Timber.v("Translation %s - %s", request.id, it.output)
                    item.addTranslation(request.targetLang!!, it.output)
                    translation = it.output
                }
            }
        }
        item.translation = translation
    }

    private fun isFavorite(word: Word): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(word.id)) {
            val favorite = hasStore(word.id, Type.WORD, Subtype.DEFAULT, State.FAVORITE)
            Timber.v("Favorite of %s %s", word.id, favorite)
            favorites.put(word.id, favorite)
        }
        return favorites.get(word.id)
    }

    private fun toggleFavorite(id: String): Boolean {
        val favorite = hasStore(id, Type.WORD, Subtype.DEFAULT, State.FAVORITE)
        if (favorite) {
            removeStore(id, Type.WORD, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, false)
        } else {
            putStore(id, Type.WORD, Subtype.DEFAULT, State.FAVORITE)
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