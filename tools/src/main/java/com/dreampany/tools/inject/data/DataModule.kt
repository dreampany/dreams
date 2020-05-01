package com.dreampany.tools.inject.data

import com.dreampany.common.inject.data.StoreModule
import com.dreampany.tools.inject.data.crypto.CryptoModule
import com.dreampany.tools.inject.data.radio.RadioModule
import dagger.Module

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        StoreModule::class,
        CryptoModule::class,
        RadioModule::class
    ]
)
class DataModule {

}