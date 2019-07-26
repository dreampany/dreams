package com.dreampany.history.injector.data

import com.dreampany.frame.injector.data.FrameModule
import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.Room
import com.dreampany.history.data.misc.HistoryMapper
import com.dreampany.history.data.source.api.HistoryDataSource
import com.dreampany.history.data.source.remote.RemoteHistoryDataSource
import com.dreampany.history.data.source.remote.WikiHistoryService
import com.dreampany.history.data.source.room.HistoryDao
import com.dreampany.history.data.source.room.RoomHistoryDataSource
import com.dreampany.history.injector.vm.ViewModelModule
import com.dreampany.history.misc.Constants
import com.dreampany.history.misc.WikiHistory
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.injector.TranslationModule
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [FrameModule::class, TranslationModule::class, DatabaseModule::class, SupportModule::class, ViewModelModule::class])
class BuildersModule {

    @Singleton
    @Provides
    @Room
    fun provideRoomHistoryDataSource(
        mapper: HistoryMapper,
        dao : HistoryDao
    ): HistoryDataSource {
        return RoomHistoryDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Remote
    fun provideRemoteHistoryDataSource(
        network: NetworkManager,
        mapper: HistoryMapper,
        service : WikiHistoryService
    ): HistoryDataSource {
        return RemoteHistoryDataSource(network, mapper, service)
    }


    @Singleton
    @Provides
    @WikiHistory
    fun provideWikiHistoryRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.Api.HISTORY_MUFFIN_LABS)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
        return retrofit;
    }

    @Provides
    fun provideWikiHistoryService(@WikiHistory retrofit: Retrofit): WikiHistoryService {
        return retrofit.create(WikiHistoryService::class.java);
    }
}
