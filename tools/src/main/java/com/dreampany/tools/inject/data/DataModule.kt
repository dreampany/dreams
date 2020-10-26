package com.dreampany.tools.inject.data

import com.dreampany.framework.inject.data.DatabaseModule
import dagger.Module

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        DatabaseModule::class,
        MiscModule::class,
        CryptoModule::class,
        RadioModule::class,
        NoteModule::class,
        HistoryModule::class,
        WifiModule::class,
        NewsModule::class
    ]
)
class DataModule {

}