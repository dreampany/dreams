package com.dreampany.tools.injector.data

import android.content.Context
import com.dreampany.framework.misc.Remote
import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.question.injector.TriviaQuestionModule
import com.dreampany.tools.api.question.remote.TriviaQuestionService
import com.dreampany.tools.data.mapper.QuestionMapper
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.data.source.api.QuestionDataSource
import com.dreampany.tools.data.source.remote.RemoteQuestionDataSource
import com.dreampany.tools.data.source.room.RoomQuestionDataSource
import com.dreampany.tools.data.source.room.dao.QuestionDao
import com.dreampany.tools.injector.annote.question.QuestionAnnote
import com.dreampany.tools.injector.annote.question.QuestionItemAnnote
import com.dreampany.tools.ui.model.question.QuestionItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2020-02-14
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        TriviaQuestionModule::class
    ]
)
class QuestionModule {
    @Singleton
    @Provides
    @QuestionAnnote
    fun provideQuestionSmartMap(): SmartMap<String, Question> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuestionAnnote
    fun provideQuestionSmartCache(): SmartCache<String, Question> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @QuestionItemAnnote
    fun provideQuestionItemSmartMap(): SmartMap<String, QuestionItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuestionItemAnnote
    fun provideQuestionItemSmartCache(): SmartCache<String, QuestionItem> {
        return SmartCache.newCache()
    }

/*    @Singleton
    @Provides
    @QuestionItemAnnote
    fun provideQuestionItemSmartMap(): SmartMap<String, QuestionItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuestionItemAnnote
    fun provideQuestionItemSmartCache(): SmartCache<String, QuestionItem> {
        return SmartCache.newCache()
    }*/

    @Room
    @Provides
    @Singleton
    fun provideRoomQuestionDataSource(
        mapper: QuestionMapper,
        dao: QuestionDao
    ): QuestionDataSource {
        return RoomQuestionDataSource(mapper, dao)
    }

    @Remote
    @Provides
    @Singleton
    fun provideRemoteQuestionDataSource(
        context: Context,
        network: NetworkManager,
        mapper: QuestionMapper,
        service: TriviaQuestionService
    ): QuestionDataSource {
        return RemoteQuestionDataSource(context, network, mapper, service)
    }
}