package com.dreampany.tools.injector.data

import com.dreampany.tools.api.question.injector.TriviaQuestionModule
import dagger.Module

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
}