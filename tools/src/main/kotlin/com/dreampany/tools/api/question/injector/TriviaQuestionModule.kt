package com.dreampany.tools.api.question.injector

import com.dreampany.tools.api.question.remote.TriviaQuestionService
import com.dreampany.tools.api.question.misc.Constants
import com.dreampany.tools.api.question.misc.QuestionAnnote
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 2020-02-13
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class TriviaQuestionModule {

    @Singleton
    @Provides
    @QuestionAnnote
    fun provideQuestionRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.Api.BASE_URL)
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideQuestionService(@QuestionAnnote retrofit: Retrofit): TriviaQuestionService {
        return retrofit.create(TriviaQuestionService::class.java);
    }
}