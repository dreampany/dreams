package com.dreampany.tube.inject.data

import com.dreampany.framework.inject.data.StoreModule
import com.dreampany.tube.api.inject.data.YoutubeModule
import dagger.Module

/**
 * Created by roman on 5/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        StoreModule::class,
        YoutubeModule::class
    ]
)
class DataModule {

}