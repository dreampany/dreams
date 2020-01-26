package com.dreampany.tools.ui.vm.word

import android.app.Application
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.PointMapper
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.PointRepository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.framework.util.DataUtilKt
import com.dreampany.framework.util.NumberUtil
import com.dreampany.framework.util.TextUtil
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.RelatedQuizMapper
import com.dreampany.tools.ui.misc.RelatedQuizRequest
import com.dreampany.tools.data.mapper.WordMapper
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.data.model.RelatedQuiz
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.RelatedQuizItem
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
class RelatedQuizViewModel
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
    private val pointMapper: PointMapper,
    private val pointRepo: PointRepository,
    private val wordMapper: WordMapper,
    private val wordRepo: WordRepository,
    private val mapper: RelatedQuizMapper
) : BaseViewModel<RelatedQuiz, RelatedQuizItem, UiTask<RelatedQuiz>>(application, rx, ex, rm) {

    fun request(request: RelatedQuizRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            //requestMultiple(request)
        }
    }

    private fun requestSingle(request: RelatedQuizRequest) {
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
                postResult(
                    state = request.state,
                    action = request.action,
                    data = result
                )
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postFailure(
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addSingleSubscription(disposable)
    }

    private fun requestMultiple(request: RelatedQuizRequest) {
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
                //postResult(request.action, result)
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

    private fun requestUiItemRx(request: RelatedQuizRequest): Maybe<RelatedQuizItem> {
        return requestItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun requestUiItemsRx(request: RelatedQuizRequest): Maybe<List<RelatedQuizItem>> {
        return requestItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemRx(request: RelatedQuizRequest): Maybe<RelatedQuiz> {
        return Maybe.create { emitter ->
            var quiz: RelatedQuiz? = null
            when (request.action) {
                Action.GET,
                Action.NEXT -> {
                    quiz = nextRelatedQuiz(request)
                }
                Action.SOLVE -> {
                    quiz = solveRelatedQuiz(request)
                }
            }
            if (emitter.isDisposed) return@create
            if (quiz == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(quiz)
            }
        }
    }

    private fun requestItemsRx(request: RelatedQuizRequest): Maybe<List<RelatedQuiz>> {
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
            // emitter.onSuccess(result)
        }
    }

    private fun getUiItemRx(
        request: RelatedQuizRequest,
        item: RelatedQuiz
    ): Maybe<RelatedQuizItem> {
        return Maybe.create { emitter ->
            val uiItem = getUiItem(request, item)
            if (emitter.isDisposed) return@create
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItemsRx(
        request: RelatedQuizRequest,
        items: List<RelatedQuiz>
    ): Maybe<List<RelatedQuizItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: RelatedQuizRequest, item: RelatedQuiz): RelatedQuizItem {
        var uiItem: RelatedQuizItem? =
            mapper.getUiItem(item.id, item.type, item.subtype, item.level)
        if (uiItem == null) {
            uiItem = RelatedQuizItem.getItem(item)
            mapper.putUiItem(uiItem)
        }
        uiItem.item = item
        uiItem.point = mapper.getPoint(item, pointMapper, pointRepo)
        uiItem.run {
            typePoint = mapper.getPointByType(this.item, pointMapper, pointRepo)
            totalPoint = mapper.getTotalPoint(this.item, pointMapper, pointRepo)
            typeCount = storeRepo.getCountByType(request.type, request.subtype, request.resolve)
            totalCount =
                typeCount + storeRepo.getCountByType(request.type, request.subtype, request.state)
        }
        return uiItem
    }

    private fun nextRelatedQuiz(request: RelatedQuizRequest): RelatedQuiz? {
        var quiz: RelatedQuiz? = null
        do {
            val store = storeRepo.getRandomItem(request.type, request.subtype, request.state)
            if (store == null) {
                return null
            }
            if (storeRepo.isExists(store.id, request.type, request.subtype, request.resolve)) {
                wordRepo.removeStore(store.id, request.type, request.subtype, request.state)
                continue
            }
            val word = wordMapper.getItem(store, wordRepo)
            if (word != null) {
                var answer: String? = null
                var options: ArrayList<String>? = null
                if (request.subtype == Subtype.SYNONYM) {
                    if (word.hasSynonyms()) {
                        answer = DataUtilKt.takeRandom<String?>(word.synonyms)
                    }
                } else if (request.subtype == Subtype.ANTONYM) {
                    if (word.hasAntonyms()) {
                        answer = DataUtilKt.takeRandom<String?>(word.antonyms)
                    }
                }
                if (answer != null) {
                    options = wordRepo.getRawItemsByLength(
                        answer,
                        (Constants.Limit.QUIZ_OPTIONS - 1).toLong()
                    ) as ArrayList<String>?
                }
                if (!options.isNullOrEmpty()) {
                    val randIndex = NumberUtil.nextRand(options.size)
                    options.add(randIndex, answer!!)

                    quiz = RelatedQuiz(
                        time = TimeUtilKt.currentMillis(),
                        id = word.id,
                        type = request.type,
                        subtype = request.subtype,
                        level = Level.A1,
                        options = options,
                        answer = answer
                    )
                }
            }
        } while (quiz == null)
        return quiz
    }

    private fun solveRelatedQuiz(request: RelatedQuizRequest): RelatedQuiz? {
        var quiz: RelatedQuiz? = request.input
        quiz?.run {
            given = request.given
            val point = mapper.getPoint(this, pointMapper, pointRepo)
            point?.run {
                pointId = id
                pointRepo.putItem(point)
            }
            wordRepo.putStore(id, request.type, request.subtype, request.resolve)
            wordRepo.removeStore(id, request.type, request.subtype, request.state)
        }
        return quiz
    }
}