package com.dreampany.nearby.inject.data

import com.dreampany.framework.inject.annote.Memory
import com.dreampany.framework.inject.annote.Room
import com.dreampany.nearby.data.source.api.ApkDataSource
import com.dreampany.nearby.data.source.mapper.ApkMapper
import com.dreampany.nearby.data.source.memory.ApkMemoryDataSource
import com.dreampany.nearby.data.source.memory.ApkProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 29/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class MediaModule {
    @Singleton
    @Provides
    @Memory
    fun provideApkMemoryDataSource(
        mapper: ApkMapper,
        provider: ApkProvider
    ): ApkDataSource = ApkMemoryDataSource(mapper, provider)
}