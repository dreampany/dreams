package com.dreampany.tools.injector.data

import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.api.question.injector.TriviaQuestionModule
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.data.model.resume.Resume
import com.dreampany.tools.injector.annote.question.QuestionAnnote
import com.dreampany.tools.injector.annote.question.QuestionItemAnnote
import com.dreampany.tools.injector.annote.resume.ResumeAnnote
import com.dreampany.tools.injector.annote.resume.ResumeItemAnnote
import com.dreampany.tools.ui.model.resume.ResumeItem
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
    fun provideResumeSmartCache(): SmartCache<String, Question> {
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
}