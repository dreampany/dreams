package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.language.Language
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.misc.WordRequest
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.translation.data.misc.TextTranslationMapper
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
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val wordPref: WordPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val translationRepo: TranslationRepository,
    private val mapper: WordMapper,
    private val repo: WordRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Word, WordItem, UiTask<Word>>(application, rx, ex, rm) {

    private lateinit var uiCallback: SmartAdapter.Callback<WordItem>

    init {
        val language = pref.getLanguage(Language.ENGLISH)
        if (!language.equals(Language.ENGLISH)) {
            //translationRepo.ready(language.code)
        }
    }

    fun setUiCallback(callback: SmartAdapter.Callback<WordItem>) {
        this.uiCallback = callback
    }

    fun request(request: WordRequest) {
        if (request.single) {
            loadSingle(request)
        } else {
            if (request.suggests) {
                loadMultipleOfString(request)
            } else {
                loadMultiple(request)
            }
        }
    }

    private fun loadSingle(request: WordRequest) {
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
                postResult(request.action, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun loadMultiple(request: WordRequest) {
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
                postResult(request.action, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun loadMultipleOfString(request: WordRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(loadItemsOfStringRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResultOfString(request.action, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun loadUiItemRx(request: WordRequest): Maybe<WordItem> {
        return getItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun loadUiItemsRx(request: WordRequest): Maybe<List<WordItem>> {
        var maybe = repo.getItemsRx()
        if (request.action == Action.SEARCH) {

        }
        return maybe.flatMap { getUiItemsRx(request, it) }
    }

    private fun loadItemsOfStringRx(request: WordRequest): Maybe<List<String>> {
        return repo.getRawWordsRx()
    }

    private fun getItemRx(request: WordRequest): Maybe<Word> {
        if (request.recent) {
            return wordPref.getRecentWordRx()
        }
        if (request.action == Action.SEARCH) {
            return repo.getItemRx(request.id!!)
        }
        return repo.getItemRx(request.id!!)
    }

    private fun getUiItemRx(request: WordRequest, item: Word): Maybe<WordItem> {
        Timber.v("Word %s", item.toString())
        return Maybe.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            if (request.history) {
                wordPref.setRecentWord(item)
                putState(item.id, Type.WORD, Subtype.DEFAULT, State.HISTORY)
            }
            emitter.onSuccess(getUiItem(request, item))
        }
    }

    private fun getUiItemsRx(request: WordRequest, items: List<Word>): Maybe<List<WordItem>> {
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
        if (request.translate) {
            adjustTranslate(request, uiItem)
        }
        return uiItem
    }

    private fun adjustFavorite(word: Word, item: WordItem) {
        item.favorite = isFavorite(word)
    }

    private fun adjustTranslate(request: WordRequest, item: WordItem) {
        var translation: String? = null
        if (request.translate) {
            if (item.hasTranslation(request.target)) {
                translation = item.getTranslationBy(request.target)
            } else {
                val textTranslation =
                    translationRepo.getItem(request.source!!, request.target!!, request.id!!)
                textTranslation?.let {
                    Timber.v("Translation %s - %s", request.id, it.output)
                    item.addTranslation(request.target!!, it.output)
                    translation = it.output
                }
            }
        }
        item.translation = translation
    }

    private fun isFavorite(word: Word): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(word.id)) {
            val favorite = hasState(word.id, Type.WORD, Subtype.DEFAULT, State.FAVOURITE)
            Timber.v("Favorite of %s %s", word.id, favorite)
            favorites.put(word.id, favorite)
        }
        return favorites.get(word.id)
    }


    private fun hasState(id: String, type: Type, subtype: Subtype, state: State): Boolean {
        return storeRepo.isExists(id, type, subtype, state)
    }

    private fun putState(id: String, type: Type, subtype: Subtype, state: State): Long {
        val store = Store(id, type, subtype, state)
        return storeRepo.putItem(store)
    }
}