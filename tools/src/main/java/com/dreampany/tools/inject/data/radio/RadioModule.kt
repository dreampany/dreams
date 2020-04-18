package com.dreampany.tools.inject.data.radio

import com.dreampany.common.inject.annote.Remote
import com.dreampany.common.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.radio.RadioBrowserModule
import com.dreampany.tools.api.radio.StationService
import com.dreampany.tools.data.source.radio.api.StationDataSource
import com.dreampany.tools.data.source.radio.mapper.StationMapper
import com.dreampany.tools.data.source.radio.remote.StationRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        RadioBrowserModule::class
    ]
)
class RadioModule {

    @Singleton
    @Provides
    @Remote
    fun provideRemoteStationDataSource(
        network: NetworkManager,
        parser: Parser,
        mapper: StationMapper,
        service: StationService
    ): StationDataSource {
        return StationRemoteDataSource(
            network,
            parser,
            mapper,
            service
        )
    }
}