package com.dreampany.tools.injector.data

import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.*
import com.dreampany.tools.data.model.word.Quiz
import com.dreampany.tools.data.model.word.RelatedQuiz
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.injector.annote.*
import com.dreampany.tools.injector.annote.app.AppAnnote
import com.dreampany.tools.injector.annote.app.AppItemAnnote
import com.dreampany.tools.injector.annote.word.*
import com.dreampany.tools.ui.model.*
import com.dreampany.tools.ui.model.word.QuizItem
import com.dreampany.tools.ui.model.word.RelatedQuizItem
import com.dreampany.tools.ui.model.word.WordItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/11/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @DemoAnnote
    fun provideDemoSmartMap(): SmartMap<String, Demo> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @DemoAnnote
    fun provideDemoSmartCache(): SmartCache<String, Demo> {
        return SmartCache.newCache()
    }


}