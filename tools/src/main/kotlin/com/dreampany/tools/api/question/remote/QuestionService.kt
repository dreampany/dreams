package com.dreampany.tools.api.question.remote

import com.dreampany.tools.api.question.misc.Constants
import com.dreampany.tools.api.question.model.TriviaQuestionsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by roman on 2020-02-13
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface QuestionService {

    @GET(value = Constants.Api.API)
    fun getQuestions(
        @Query("amount") limit: Long,
        @Query("category") category: Int = 0,
        @Query("difficulty") difficulty: String = "",
        @Query("type") type: String = ""
    ): Call<TriviaQuestionsResponse>
}