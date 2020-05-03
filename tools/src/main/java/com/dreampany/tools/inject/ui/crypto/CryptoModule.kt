package com.dreampany.tools.inject.ui.crypto

import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.tools.ui.crypto.activity.CoinActivity
import com.dreampany.tools.ui.crypto.activity.CoinsActivity
import com.dreampany.tools.ui.crypto.activity.FavoriteCoinsActivity
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

    @ActivityScope
    @ContributesAndroidInjector(modules = [CoinModule::class])
    abstract fun coin(): CoinActivity

    @ContributesAndroidInjector
    abstract fun favorites(): FavoriteCoinsActivity
}