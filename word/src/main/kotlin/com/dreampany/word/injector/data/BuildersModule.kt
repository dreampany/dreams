package com.dreampany.word.injector.data

import android.content.Context
import com.dreampany.firebase.RxFirebaseFirestore
import com.dreampany.word.injector.vm.ViewModelModule
import com.dreampany.frame.injector.data.FrameModule
import com.dreampany.frame.misc.*
import com.dreampany.network.manager.NetworkManager
import com.dreampany.vision.VisionApi
import com.dreampany.word.api.wordnik.WordnikManager
import com.dreampany.word.data.misc.WordMapper
import com.dreampany.word.data.source.api.WordDataSource
import com.dreampany.word.data.source.assets.WordAssetsDataSource
import com.dreampany.word.data.source.firestore.WordFirestoreDataSource
import com.dreampany.word.data.source.remote.WordRemoteDataSource
import com.dreampany.word.data.source.room.AntonymDao
import com.dreampany.word.data.source.room.SynonymDao
import com.dreampany.word.data.source.room.WordDao
import com.dreampany.word.data.source.room.WordRoomDataSource
import com.dreampany.word.data.source.vision.WordVisionDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [FrameModule::class, DatabaseModule::class, SupportModule::class, ViewModelModule::class])
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
        return WordRoomDataSource(mapper, dao, synonymDao, antonymDao)
    }

    @Singleton
    @Provides
    @Firestore
    fun provideWordFirestoreDataSource(network: NetworkManager,
                                       firestore: RxFirebaseFirestore
    ): WordDataSource {
        return WordFirestoreDataSource(network, firestore)
    }

    @Singleton
    @Provides
    @Remote
    fun provideWordRemoteDataSource(network: NetworkManager,
                                    mapper: WordMapper,
                                    wordnik: WordnikManager
    ): WordDataSource {
        return WordRemoteDataSource(network, mapper, wordnik)
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
