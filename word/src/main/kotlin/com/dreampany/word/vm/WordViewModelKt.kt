package com.dreampany.word.vm

import android.app.Application
import com.annimon.stream.Stream
import com.dreampany.frame.data.enums.Language
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
import com.dreampany.word.data.misc.StateMapper
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.model.WordRequest
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.data.source.repository.ApiRepository
import com.dreampany.word.misc.Constants
import com.dreampany.word.ui.model.UiTask
import com.dreampany.word.ui.model.WordItem
import io.reactivex.Maybe
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordViewModelKt @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    val network: NetworkManager,
    val pref: Pref,
    val stateMapper: StateMapper,
    val repo: ApiRepository,
    val translationRepo: TranslationRepository
) : BaseViewModel<Word, WordItem, UiTask<Word>>(application, rx, ex, rm) {

    fun load(request: WordRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain(findItemRx(request))
            .doOnSubscribe { subscription ->
                if (!pref.isLoaded) {
                    updateUiState(UiState.NONE)
                }
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                if (!DataUtil.isEmpty(result)) {
                    pref.commitLoaded()
                }
                postResult(Response.Type.SEARCH, result)
                //getEx().postToUi(() -> update(false), 3000L);
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    fun getCurrentLanguage(): Language {
        return pref.getLanguage(Language.ENGLISH)
    }

    fun setCurrentLanguage(language: Language) {
        pref.setLanguage(language)
    }

    fun getLanguages(): ArrayList<Language> {
        val result = ArrayList<Language>()
        result.add(Language.ARABIC)
        result.add(Language.BENGALI)
        result.add(Language.FRENCH)
        result.add(Language.SPANISH)
        result.add(Language.ENGLISH)
        return result
    }

    fun needToTranslate(): Boolean {
        val language = getCurrentLanguage()
        return language != Language.ENGLISH
    }

    fun getLanguageDirection(): String {
        val language = getCurrentLanguage()
        return Language.ENGLISH.code + Constants.Sep.HYPHEN + language.code
    }

    private fun findItemRx(request: WordRequest): Maybe<WordItem> {
        return Maybe.create { emitter ->
            val word = repo.getItem(request.inputWord!!, false)
            val fullWord = repo.getItemIf(word)
            var result: WordItem? = null
            if (fullWord != null) {
                pref.lastSearchWord = fullWord
                result = getItem(request, fullWord, true)
            }

            if (!emitter.isDisposed) {
                if (result == null) {
                    emitter.onError(EmptyException())
                } else {
                    emitter.onSuccess(result)
                }
            }
        }
    }

    private fun getItem(request: WordRequest, word: Word, fully: Boolean): WordItem {
        val map = uiMap
        var item: WordItem? = map.get(word.id)
        if (item == null) {
            item = WordItem.getSimpleItem(word)
            map.put(word.id, item)
        }
        item!!.setItem(word)
        adjustFavorite(word, item)
        if (fully) {
            adjustState(item)
        }
        if (request.translate) {
            adjustTranslate(request, item)
        }
        return item
    }

    private fun adjustFavorite(word: Word, item: WordItem) {
        item.isFavorite = repo.isFavorite(word)
    }

    private fun adjustState(item: WordItem) {
        val states = repo.getStates(item.item)
        Stream.of(states).forEach { state -> item.addState(stateMapper.toState(state.state)) }
    }

    private fun adjustTranslate(request: WordRequest, item: WordItem) {
        if (request.translate && !item.hasTranslation(request.target)) {
            val translation =
                translationRepo.getItem(request.inputWord!!, request.source!!, request.target!!)
            Timber.v("Translation %s - %s", request.inputWord, translation)
            translation?.let {
                item.addTranslation(request.target!!, it.output)
                item.translation = it.output
            }
        }
    }
}