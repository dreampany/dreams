package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.ui.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.misc.QuizMapper
import com.dreampany.tools.data.misc.QuizRequest
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.ui.model.QuizItem
import com.dreampany.tools.util.Util
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by roman on 2019-08-31
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuizViewModel
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
    private val mapper: QuizMapper
) : BaseViewModel<Quiz, QuizItem, UiTask<Quiz>>(application, rx, ex, rm) {

    fun request(request: QuizRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestSingle(request: QuizRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemRx(request))
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

    private fun requestMultiple(request: QuizRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemsRx(request))
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

    private fun requestUiItemRx(request: QuizRequest): Maybe<QuizItem> {
        return Maybe.empty()
    }

    private fun requestUiItemsRx(request: QuizRequest): Maybe<List<QuizItem>> {
        /*return Maybe.empty() *//*storeRepo.getItemsRx(Type.QUIZ, Subtype.RELATED, State.DEFAULT)
            .flatMap { getUiItemsOfStoresRx(request, it) }*/

        return requestItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemsRx(request: QuizRequest): Maybe<List<Quiz>> {
        return Maybe.create { emitter ->
            val result = mutableListOf<Quiz>()
            result.add(
                Quiz(
                    id = Util.concat(Type.QUIZ, Subtype.SYNONYM),
                    type = Type.QUIZ,
                    subtype = Subtype.SYNONYM,
                    title = TextUtil.toTitleCase(Subtype.SYNONYM.name)
                )
            )
            result.add(
                Quiz(
                    id = Util.concat(Type.QUIZ, Subtype.ANTONYM),
                    type = Type.QUIZ,
                    subtype = Subtype.ANTONYM,
                    title = TextUtil.toTitleCase(Subtype.ANTONYM.name)
                )
            )
            if (emitter.isDisposed) {
                return@create
            }
            emitter.onSuccess(result)
        }
    }

    private fun getUiItemsRx(request: QuizRequest, items: List<Quiz>): Maybe<List<QuizItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: QuizRequest, item: Quiz): QuizItem {
        var uiItem: QuizItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = QuizItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        return uiItem
    }
}