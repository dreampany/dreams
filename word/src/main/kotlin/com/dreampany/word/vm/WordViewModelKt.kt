package com.dreampany.word.vm

import android.app.Application
import com.annimon.stream.Stream
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.word.data.misc.StateMapper
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.model.WordRequest
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.data.source.repository.ApiRepository
import com.dreampany.word.ui.model.UiTask
import com.dreampany.word.ui.model.WordItem
import io.reactivex.Maybe
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
    val repo: ApiRepository
) : BaseViewModel<Word, WordItem, UiTask<Word>>(application, rx, ex, rm) {

    fun load(request: WordRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain(findItemRx(request.inputWord!!))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
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


    private fun findItemRx(query: String): Maybe<WordItem> {
        return Maybe.create { emitter ->
            val word = repo.getItem(query, false)
            val fullWord = repo.getItemIf(word)
            var result: WordItem? = null
            if (fullWord != null) {
                pref.lastSearchWord = fullWord
                result = getItem(fullWord, true)
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

    private fun getItem(word: Word, fully: Boolean): WordItem {
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
        return item
    }

    private fun adjustFavorite(word: Word, item: WordItem) {
        item.isFavorite = repo.isFavorite(word)
    }

    private fun adjustState(item: WordItem) {
        val states = repo.getStates(item.item)
        Stream.of(states).forEach { state -> item.addState(stateMapper.toState(state.state)) }
    }
}