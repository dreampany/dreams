package com.dreampany.news.inject.data

import com.dreampany.framework.inject.data.DatabaseModule
import dagger.Module

/**
 * Created by roman on 5/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        DatabaseModule::class,
        MiscModule::class
    ]
)
class DataModule {
}