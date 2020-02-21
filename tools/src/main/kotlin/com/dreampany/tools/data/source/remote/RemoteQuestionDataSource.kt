package com.dreampany.tools.data.source.remote

import android.content.Context
import com.dreampany.framework.data.enums.Difficult
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.extension.toTitle
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.question.model.TriviaQuestionsResponse
import com.dreampany.tools.api.question.remote.TriviaQuestionService
import com.dreampany.tools.api.radio.RadioStation
import com.dreampany.tools.data.mapper.QuestionMapper
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.data.source.api.QuestionDataSource
import io.reactivex.Maybe
import retrofit2.Call
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RemoteQuestionDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val mapper: QuestionMapper,
    private val service: TriviaQuestionService
) : QuestionDataSource {
    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Question): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Question): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Question): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Question): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Question>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Question>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Question): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Question>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Question): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Question>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Question? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(
        category: Question.Category?,
        type: Question.Type?,
        difficult: Difficult?,
        limit: Long
    ): List<Question>? {
        if (!network.hasInternet()) {
            return null
        }
        try {
            var callee: Call<TriviaQuestionsResponse>? = null
            if (category != null) {
                if (type != null) {
                    if (difficult != null) {
                        callee = service.getQuestions(
                            category = category.code,
                            type = type.code,
                            difficult = difficult.name.toLowerCase(Locale.getDefault()),
                            limit = limit
                        )
                    }
                } else if (difficult != null) {
                    callee = service.getQuestions(
                        category = category.code,
                        difficult = difficult.name.toLowerCase(Locale.getDefault()),
                        limit = limit
                    )
                } else {
                    callee = service.getQuestions(
                        category = category.code,
                        limit = limit
                    )
                }
            } else if (type != null) {
                if (difficult != null) {
                    callee = service.getQuestions(
                        type = type.code,
                        difficult = difficult.name.toLowerCase(Locale.getDefault()),
                        limit = limit
                    )
                } else {
                    callee = service.getQuestions(type = type.code, limit = limit)
                }
            } else if (difficult != null) {
                callee = service.getQuestions(
                    difficult = difficult.name.toLowerCase(Locale.getDefault()),
                    limit = limit
                )
            } else {
                callee = service.getQuestions(limit = limit)
            }

            val response = callee?.execute() ?: return null
            if (response.isSuccessful) {
                val response: TriviaQuestionsResponse = response.body() ?: return null
                val result = mapper.getItems(response.results)
                return result
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return null
    }

    override fun getItems(): List<Question>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Question>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(
        category: Question.Category?,
        type: Question.Type?,
        difficult: Difficult?,
        limit: Long
    ): Maybe<List<Question>> {
        return Maybe.create { emitter ->
            val result = getItems(category, type, difficult, limit)
            if (emitter.isDisposed) return@create
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItemsRx(): Maybe<List<Question>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Question>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}