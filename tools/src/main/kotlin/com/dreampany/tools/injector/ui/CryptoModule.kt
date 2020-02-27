package com.dreampany.tools.injector.ui

import com.dreampany.framework.injector.annote.FragmentScope
import com.dreampany.tools.ui.fragment.crypto.CryptoFragment
import com.dreampany.tools.ui.fragment.crypto.CryptoHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class CryptoModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun cryptoHomeFragment(): CryptoHomeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [CryptoInfoModule::class])
    abstract fun cryptoFragment(): CryptoFragment
}