package com.dreampany.tools.injector.ui.crypto

import com.dreampany.tools.ui.fragment.crypto.CryptoMarketFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2/28/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class CryptoMarketModule {
    @ContributesAndroidInjector
    abstract fun marketFragment(): CryptoMarketFragment
}