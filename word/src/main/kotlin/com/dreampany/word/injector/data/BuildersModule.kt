package com.dreampany.word.injector.data

import android.content.Context
import com.dreampany.firebase.RxFirebaseFirestore
import com.dreampany.framework.injector.data.FrameModule
import com.dreampany.framework.misc.*
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.injector.TranslationModule
import com.dreampany.vision.VisionApi
import com.dreampany.word.api.wordnik.WordnikManager
import com.dreampany.word.data.misc.WordMapper
import com.dreampany.word.data.source.api.WordDataSource
import com.dreampany.word.data.source.assets.AssetsWordDataSource
import com.dreampany.word.data.source.firestore.FirestoreWordDataSource
import com.dreampany.word.data.source.remote.RemoteWordDataSource
import com.dreampany.word.data.source.room.AntonymDao
import com.dreampany.word.data.source.room.RoomWordDataSource
import com.dreampany.word.data.source.room.SynonymDao
import com.dreampany.word.data.source.room.WordDao
import com.dreampany.word.data.source.vision.VisionWordDataSource
import com.dreampany.word.injector.vm.ViewModelModule
import dagger.Module
import dagger.Provides
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
    @Assets
    fun provideAssetsWordDataSource(context: Context,
                                    mapper: WordMapper
    ): WordDataSource {
        return AssetsWordDataSource(context, mapper)
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomWordDataSource(mapper: WordMapper,
                                  dao: WordDao,
                                  synonymDao: SynonymDao,
                                  antonymDao: AntonymDao
    ): WordDataSource {
        return RoomWordDataSource(
            mapper,
            dao,
            synonymDao,
            antonymDao
        )
    }

    @Singleton
    @Provides
    @Firestore
    fun provideFirestoreWordDataSource(network: NetworkManager,
                                       firestore: RxFirebaseFirestore
    ): WordDataSource {
        return FirestoreWordDataSource(network, firestore)
    }

    @Singleton
    @Provides
    @Remote
    fun provideRemoteWordDataSource(network: NetworkManager,
                                    mapper: WordMapper,
                                    wordnik: WordnikManager
    ): WordDataSource {
        return RemoteWordDataSource(network, mapper, wordnik)
    }

    @Singleton
    @Provides
    @Vision
    fun provideVisionWordDataSource(mapper: WordMapper,
                                    vision: VisionApi
    ): WordDataSource {
        return VisionWordDataSource(mapper, vision)
    }
}
