package com.dreampany.tools.inject.ui.crypto

import com.dreampany.tools.ui.crypto.activity.CoinsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class CryptoModule {
    @ContributesAndroidInjector
    abstract fun coins(): CoinsActivity
}