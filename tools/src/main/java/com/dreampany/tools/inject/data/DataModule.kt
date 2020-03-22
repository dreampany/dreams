package com.dreampany.tools.inject.data

import com.dreampany.tools.inject.data.crypto.CryptoModule
import dagger.Module

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        CryptoModule::class
    ]
)
class DataModule {

}