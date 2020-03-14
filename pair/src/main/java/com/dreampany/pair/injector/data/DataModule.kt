package com.dreampany.pair.injector.data

import com.dreampany.common.injector.annote.RemoteAnnote
import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.source.api.RegistrationDataSource
import com.dreampany.pair.data.source.remote.RegistrationRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class DataModule {

    @Singleton
    @Provides
    @RemoteAnnote
    fun provideRegistrationRemoteDataSource(
        mappers: Mappers
    ): RegistrationDataSource {
        return RegistrationRemoteDataSource(mappers)
    }
}