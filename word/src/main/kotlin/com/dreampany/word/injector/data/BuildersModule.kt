package com.dreampany.word.injector.data

import android.content.Context
import com.dreampany.firebase.RxFirebaseFirestore
import com.dreampany.word.injector.vm.ViewModelModule
import com.dreampany.frame.injector.data.FrameModule
import com.dreampany.frame.misc.*
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.injector.TranslationModule
import com.dreampany.vision.VisionApi
import com.dreampany.word.api.wordnik.WordnikManager
import com.dreampany.word.data.misc.WordMapper
import com.dreampany.word.data.source.api.WordDataSource
import com.dreampany.word.data.source.assets.WordAssetsDataSource
import com.dreampany.word.data.source.firestore.FirestoreWordDataSource
import com.dreampany.word.data.source.remote.RemoteWordDataSource
import com.dreampany.word.data.source.room.AntonymDao
import com.dreampany.word.data.source.room.SynonymDao
import com.dreampany.word.data.source.room.WordDao
import com.dreampany.word.data.source.room.RoomWordDataSource
import com.dreampany.word.data.source.vision.WordVisionDataSource
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
    fun provideWordAssetsDataSource(context: Context,
                                    mapper: WordMapper
    ): WordDataSource {
        return WordAssetsDataSource(context, mapper)
    }

    @Singleton
    @Provides
    @Room
    fun provideWordRoomDataSource(mapper: WordMapper,
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
    fun provideWordFirestoreDataSource(network: NetworkManager,
                                       firestore: RxFirebaseFirestore
    ): WordDataSource {
        return FirestoreWordDataSource(network, firestore)
    }

    @Singleton
    @Provides
    @Remote
    fun provideWordRemoteDataSource(network: NetworkManager,
                                    mapper: WordMapper,
                                    wordnik: WordnikManager
    ): WordDataSource {
        return RemoteWordDataSource(network, mapper, wordnik)
    }

    @Singleton
    @Provides
    @Vision
    fun provideWordVisionDataSource(mapper: WordMapper,
                                    vision: VisionApi
    ): WordDataSource {
        return WordVisionDataSource(mapper, vision)
    }
}
