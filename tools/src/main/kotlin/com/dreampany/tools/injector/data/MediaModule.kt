package com.dreampany.tools.injector.data

import android.content.Context
import com.dreampany.frame.misc.Memory
import com.dreampany.tools.data.misc.AppMapper
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.source.api.MediaDataSource
import com.dreampany.tools.data.source.memory.MemoryAppMediaDataSource
import com.dreampany.tools.data.source.memory.provider.AppProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class MediaModule {

    @Singleton
    @Provides
    @Memory
    fun provideMemoryApkMediaDataSource(
        context: Context,
        mapper: AppMapper,
        provider: AppProvider
    ): MediaDataSource<App> {
        return MemoryAppMediaDataSource(context, mapper, provider)
    }
}