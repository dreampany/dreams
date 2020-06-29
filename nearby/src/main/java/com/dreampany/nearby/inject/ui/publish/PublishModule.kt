package com.dreampany.nearby.inject.ui.publish

import com.dreampany.nearby.ui.publish.fragment.ApksFragment
import com.dreampany.nearby.ui.publish.fragment.ImagesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class PublishModule {
    @ContributesAndroidInjector
    abstract fun apk(): ApksFragment

    @ContributesAndroidInjector
    abstract fun image(): ImagesFragment
}