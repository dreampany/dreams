package com.dreampany.word.vm

import android.app.Application
import androidx.fragment.app.Fragment
import com.annimon.stream.Stream
import com.dreampany.frame.data.enums.Language
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.model.State
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
import com.dreampany.word.data.enums.ItemState
import com.dreampany.word.data.enums.ItemSubtype
import com.dreampany.word.data.enums.ItemType
import com.dreampany.word.data.misc.StateMapper
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.model.WordRequest
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.data.source.repository.ApiRepository
import com.dreampany.word.data.source.repository.WordRepository
import com.dreampany.word.misc.Constants
import com.dreampany.word.ui.model.UiTask
import com.dreampany.word.ui.model.WordItem
import com.dreampany.word.util.Util
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
    val stateRepo: StateRepository,
    val wordRepo: WordRepository,
    val translationRepo: TranslationRepository,
    @Favorite val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Word, WordItem, UiTask<Word>>(application, rx, ex, rm) {

    private var uiCallback: SmartAdapter.Callback<WordItem>? = null

    fun setUiCallback(callback: SmartAdapter.Callback<WordItem>) {
        this.uiCallback = callback
    }

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
                if (request.recentWord) {
                    postResult(Response.Type.GET, result)
                } else {
                    postResult(Response.Type.SEARCH, result)
                }
                //getEx().postToUi(() -> update(false), 3000L);
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    fun suggests(progress: Boolean) {
        if (!takeAction(true, multipleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain<List<String>>(getSuggestionsRx())
            .doOnSubscribe({ subscription ->
                if (progress) {
                    postProgress(true)
                }
            })
            .subscribe({ result ->
                if (progress) {
                    postProgress(false)
                }
                postResultOfString(Response.Type.SUGGESTS, result)
            }, { error ->
                if (progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addMultipleSubscriptionOfString(disposable)
    }

    fun toggleFavorite(word: Word) {
        if (hasDisposable(multipleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain(toggleImpl(word))
            .subscribe({ result ->
                postResult(Response.Type.UPDATE, result, false) }, { this.postFailure(it) })
    }

    fun getCurrentLanguage(): Language {
        return pref.getLanguage(Language.ENGLISH)
    }

    fun setCurrentLanguage(language: Language) {
        pref.setLanguage(language)
    }

    fun isDefaultLanguage(): Boolean {
        return if (Language.ENGLISH == getCurrentLanguage()) true else false
    }

    fun getLanguages(): ArrayList<Language> {
        val result = ArrayList<Language>()
        result.add(Language.ARABIC)
        result.add(Language.BENGALI)
        result.add(Language.CHINESE)
        result.add(Language.HINDI)
        result.add(Language.FRENCH)
        result.add(Language.RUSSIA)
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

    fun share(fragment: Fragment) {
        val word = task!!.input
        val subject = word.id
        val text = Util.getText(word)
        AndroidUtil.share(fragment, subject, text)
    }

    private fun findItemRx(request: WordRequest): Maybe<WordItem> {
        return Maybe.create { emitter ->

            var result: WordItem? = null

            if (request.recentWord) {
                val fullWord = pref.recentWord
                if (fullWord != null) {
                    request.inputWord = fullWord.id
                    result = getItem(request, fullWord, true)
                }
            } else {
                val word = wordRepo.getItem(request.inputWord!!, false)
                val fullWord = getItemIf(word)
                if (fullWord != null) {
                    pref.recentWord = fullWord
                    result = getItem(request, fullWord, true)
                }
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

    private fun getSuggestionsRx(): Maybe<List<String>> {
        return wordRepo.getRawWordsRx()
    }

    private fun toggleImpl(word: Word): Maybe<WordItem> {
        return Maybe.fromCallable {
            toggleFavorite(word.id)
            getItem(null, word, true)
        }
    }

    private fun getItem(request: WordRequest?, word: Word, fully: Boolean): WordItem {
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
        if (request != null) {
            if (request.translate) {
                adjustTranslate(request, item)
            }
        }
        return item
    }

    private fun adjustFavorite(word: Word, item: WordItem) {
        item.isFavorite = isFavorite(word)
    }

    private fun adjustState(item: WordItem) {
        val states = getStates(item.item)
        Stream.of(states).forEach { state -> item.addState(stateMapper.toState(state.state)) }
    }

    private fun adjustTranslate(request: WordRequest, item: WordItem) {
        var translation: String? = null
        if (request.translate) {
            if (item.hasTranslation(request.target)) {
                translation = item.getTranslationBy(request.target)
            } else {
                val textTranslation =
                    translationRepo.getItem(request.inputWord!!, request.source!!, request.target!!)
                Timber.v("Translation %s - %s", request.inputWord, translation)
                textTranslation?.let {
                    item.addTranslation(request.target!!, it.output)
                    translation = it.output
                }
            }
        }
        item.translation = translation
    }

    fun getItemIf(word: Word): Word? {
        var result = getRoomItemIf(word)
        if (result == null) {
            result = getFirestoreItemIf(word)
        }
        if (result == null) {
            result = getRemoteItemIf(word)
        }
        return result
    }

    fun hasState(word: Word, subtype: ItemSubtype): Boolean {
        return stateRepo.getCountById(word.id, ItemType.WORD.name, subtype.name) > 0
    }

    fun hasState(word: Word, subtype: ItemSubtype, state: ItemState): Boolean {
        return stateRepo.getCountById(word.id, ItemType.WORD.name, subtype.name, state.name) > 0
    }

    fun hasState(id: String, subtype: ItemSubtype, state: ItemState): Boolean {
        return stateRepo.getCountById(id, ItemType.WORD.name, subtype.name, state.name) > 0
    }

    fun putState(word: Word, subtype: ItemSubtype, state: ItemState): Long {
        val s = State(word.id, ItemType.WORD.name, subtype.name, state.name)
        s.time = TimeUtil.currentTime()
        return stateRepo.putItem(s)
    }

    fun putState(id: String, subtype: ItemSubtype, state: ItemState): Long {
        val s = State(id, ItemType.WORD.name, subtype.name, state.name)
        s.time = TimeUtil.currentTime()
        return stateRepo.putItem(s)
    }

    private fun getRoomItemIf(word: Word): Word? {
        return if (!hasState(word, ItemSubtype.DEFAULT, ItemState.FULL)) {
            null
        } else wordRepo.getRoomItem(word.id, true)
    }

    private fun getFirestoreItemIf(word: Word): Word? {
        val result = wordRepo.getFirestoreItem(word.id, true)
        if (result != null) {
            Timber.v("Firestore result success")
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL)
        }
        return result
    }

    private fun getRemoteItemIf(word: Word): Word? {
        val result = wordRepo.getRemoteItem(word.id, true)
        if (result != null) {
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL)
            wordRepo.putFirestoreItem(result)
        }
        return result
    }

    fun putItem(word: Word, subtype: ItemSubtype, state: ItemState): Long {
        var result = wordRepo.putItem(word)
        if (result != -1L) {
            result = putState(word, subtype, state)
        }
        return result
    }

    fun toggleFavorite(id: String): Boolean {
        val favorite = hasState(id, ItemSubtype.DEFAULT, ItemState.FAVORITE)
        if (favorite) {
            removeState(id, ItemSubtype.DEFAULT, ItemState.FAVORITE)
            favorites.put(id, false)
        } else {
            putState(id, ItemSubtype.DEFAULT, ItemState.FAVORITE)
            favorites.put(id, true)
        }
        return favorites.get(id)
    }

    fun removeState(id: String, subtype: ItemSubtype, state: ItemState): Int {
        val s = State(id, ItemType.WORD.name, subtype.name, state.name)
        s.time = TimeUtil.currentTime()
        return stateRepo.delete(s)
    }

    fun isFavorite(word: Word): Boolean {
        if (!favorites.contains(word.id)) {
            val favorite = hasState(word, ItemSubtype.DEFAULT, ItemState.FAVORITE)
            favorites.put(word.id, favorite)
        }
        return favorites.get(word.id)
    }

    fun getStates(word: Word): List<State> {
        return stateRepo.getItems(word.id, ItemType.WORD.name, ItemSubtype.DEFAULT.name)
    }
}