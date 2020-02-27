package com.dreampany.tools.injector.ui

import com.dreampany.tools.ui.fragment.crypto.CryptoInfoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2/28/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class CryptoInfoModule {
    @ContributesAndroidInjector
    abstract fun infoFragment(): CryptoInfoFragment
}