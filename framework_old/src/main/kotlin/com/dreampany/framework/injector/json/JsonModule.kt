package com.dreampany.framework.injector.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class JsonModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        //builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        //builder.setLenient()
        return builder.create()
    }
}