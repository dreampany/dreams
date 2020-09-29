package com.dreampany.hello.inject.data

import com.dreampany.framework.inject.data.DatabaseModule
import dagger.Module

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        DatabaseModule::class,
        AuthModule::class,
        UserModule::class
    ]
)
class DataModule {
}