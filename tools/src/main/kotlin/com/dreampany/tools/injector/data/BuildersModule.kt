package com.dreampany.tools.injector.data

import com.dreampany.framework.injector.data.FrameModule
import com.dreampany.tools.injector.vm.ViewModelModule
import com.dreampany.translation.injector.TranslationModule
import dagger.Module


/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        FrameModule::class,
        SupportModule::class,
        TranslationModule::class,
        DatabaseModule::class,
        ViewModelModule::class,
        ApiModule::class,
        MediaModule::class,
        AppModule::class,
        NoteModule::class,
        WordModule::class,
        VpnModule::class,
        RadioModule::class,
        CryptoModule::class,
        BlockModule::class,
        ResumeModule::class,
        QuestionModule::class
    ]
)
class BuildersModule {
}
