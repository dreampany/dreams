package com.dreampany.common.inject.property

import android.content.Context
import com.dreampany.common.data.model.Property
import com.dreampany.common.inject.http.HttpModule
import com.dreampany.common.inject.json.JsonModule
import com.dreampany.common.misc.extension.isDebug
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        JsonModule::class,
        HttpModule::class
    ]
)
class PropertyModule {
    @Singleton
    @Provides
    fun provideProperty(context: Context): Property {
        val debug = context.isDebug()
        return Property(debug)
    }
}