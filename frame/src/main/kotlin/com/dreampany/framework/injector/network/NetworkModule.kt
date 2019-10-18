package com.dreampany.framework.injector.network

import android.content.Context
import com.dreampany.framework.data.source.api.RemoteService
import com.dreampany.framework.misc.Constants
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(context: Context): Cache {
        val cacheSize: Long = 10 * 1024 * 1024
        return Cache(context.getCacheDir(), cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return builder.create()
    }

    @Provides
    @Singleton
    fun provideConnectionPool() : ConnectionPool {
        return ConnectionPool()
    }

    @Provides
    @Singleton
    fun provideHttpClient(cache: Cache): OkHttpClient {
/*        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val httpCacheDirectory = File(context.getCacheDir(), "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())

        val networkCacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())

            var cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.MINUTES)
                    .build()

            response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
        }
*/
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            //.cache(cache)
            //.addNetworkInterceptor(networkCacheInterceptor)
            .addInterceptor(interceptor)
            .build()

        return httpClient
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .baseUrl(Constants.Api.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteService(retrofit: Retrofit): RemoteService {
        return retrofit.create(RemoteService::class.java);
    }
}