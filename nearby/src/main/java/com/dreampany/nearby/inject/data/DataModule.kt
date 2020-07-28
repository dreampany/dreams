package com.dreampany.nearby.inject.data

import android.content.Context
import com.dreampany.framework.inject.annote.Nearby
import com.dreampany.framework.inject.data.DatabaseModule
import com.dreampany.nearby.data.source.api.UserDataSource
import com.dreampany.nearby.data.source.mapper.UserMapper
import com.dreampany.nearby.data.source.nearby.UserNearbyDataSource
import com.dreampany.network.nearby.NearbyManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 22/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        DatabaseModule::class,
        MediaModule::class
    ]
)
class DataModule {
    @Singleton
    @Provides
    @Nearby
    fun provideUserNearbyDataSource(
        context: Context,
        mapper: UserMapper,
        nearby: NearbyManager
    ): UserDataSource = UserNearbyDataSource(context, mapper, nearby)
}