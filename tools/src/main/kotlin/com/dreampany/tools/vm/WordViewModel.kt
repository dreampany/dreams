package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.model.UiTask
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
import io.reactivex.Flowable
import io.reactivex.Maybe
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
        return getItemRx(request).flatMap { getUiItemRx(it) }
    }

    private fun loadUiItemsRx(request: WordRequest): Maybe<List<WordItem>> {
        var maybe = repo.getItemsRx()
        if (request.action == Action.SEARCH) {

        }
        return maybe.flatMap { getUiItemsRx(it) }
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

    private fun getUiItemRx(item: Word): Maybe<WordItem> {
        return Maybe.create { emitter ->
            emitter.onSuccess(getUiItem(item))
        }
    }

    private fun getUiItemsRx(items: List<Word>): Maybe<List<WordItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(item: Word): WordItem {
        return WordItem.getItem(item)
    }
}