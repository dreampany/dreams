package com.dreampany.tools.injector.data

import android.content.Context
import com.dreampany.firebase.RxFirebaseFirestore
import com.dreampany.framework.injector.annote.*
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.wordnik.WordnikManager
import com.dreampany.tools.data.mapper.WordMapper
import com.dreampany.tools.data.model.word.Quiz
import com.dreampany.tools.data.model.word.RelatedQuiz
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.data.source.assets.AssetsWordDataSource
import com.dreampany.tools.data.source.room.dao.AntonymDao
import com.dreampany.tools.data.source.room.dao.SynonymDao
import com.dreampany.tools.data.source.room.dao.WordDao
import com.dreampany.tools.data.source.firestore.FirestoreWordDataSource
import com.dreampany.tools.data.source.remote.RemoteWordDataSource
import com.dreampany.tools.data.source.room.RoomWordDataSource
import com.dreampany.tools.data.source.vision.VisionWordDataSource
import com.dreampany.tools.injector.annote.word.*
import com.dreampany.tools.ui.model.word.QuizItem
import com.dreampany.tools.ui.model.word.RelatedQuizItem
import com.dreampany.tools.ui.model.word.WordItem
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
class WordModule {

    @Singleton
    @Provides
    @WordAnnote
    fun provideWordSmartMap(): SmartMap<String, Word> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @WordAnnote
    fun provideWordSmartCache(): SmartCache<String, Word> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @WordItemAnnote
    fun provideWordItemSmartMap(): SmartMap<String, WordItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @WordItemAnnote
    fun provideWordItemSmartCache(): SmartCache<String, WordItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @QuizAnnote
    fun provideQuizSmartMap(): SmartMap<String, Quiz> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuizAnnote
    fun provideQuizSmartCache(): SmartCache<String, Quiz> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @QuizItemAnnote
    fun provideQuizItemSmartMap(): SmartMap<String, QuizItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuizItemAnnote
    fun provideQuizItemSmartCache(): SmartCache<String, QuizItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @RelatedQuizAnnote
    fun provideRelatedQuizSmartMap(): SmartMap<String, RelatedQuiz> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @RelatedQuizAnnote
    fun provideRelatedQuizSmartCache(): SmartCache<String, RelatedQuiz> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @RelatedQuizItemAnnote
    fun provideRelatedQuizItemSmartMap(): SmartMap<String, RelatedQuizItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @RelatedQuizItemAnnote
    fun provideRelatedQuizItemSmartCache(): SmartCache<String, RelatedQuizItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @Assets
    fun provideAssetsWordDataSource(
        context: Context,
        mapper: WordMapper
    ): WordDataSource {
        return AssetsWordDataSource(context, mapper)
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomWordDataSource(
        mapper: WordMapper,
        dao: WordDao,
        synonymDao: SynonymDao,
        antonymDao: AntonymDao
    ): WordDataSource {
        return RoomWordDataSource(mapper, dao, synonymDao, antonymDao)
    }

    @Singleton
    @Provides
    @Firestore
    fun provideFirestoreWordDataSource(
        network: NetworkManager,
        firestore: RxFirebaseFirestore
    ): WordDataSource {
        return FirestoreWordDataSource(network, firestore)
    }

    @Singleton
    @Provides
    @Remote
    fun provideRemoteWordDataSource(
        network: NetworkManager,
        mapper: WordMapper,
        wordnik: WordnikManager
    ): WordDataSource {
        return RemoteWordDataSource(network, mapper, wordnik)
    }

    @Singleton
    @Provides
    @Vision
    fun provideVisionWordDataSource(
        mapper: WordMapper,
        dao: WordDao
    ): WordDataSource {
        return VisionWordDataSource(mapper, dao)
    }
}