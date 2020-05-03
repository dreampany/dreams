package com.dreampany.framework.inject.property

import android.content.Context
import com.dreampany.framework.data.model.Property
import com.dreampany.framework.inject.http.HttpModule
import com.dreampany.framework.inject.json.JsonModule
import com.dreampany.framework.misc.extension.isDebug
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
        val debug = context.isDebug
        return Property(debug)
    }
}