package com.dreampany.tube.inject.ui.home

import com.dreampany.tube.ui.home.fragment.VideosFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class VideoModule {
    @ContributesAndroidInjector
    abstract fun videos(): VideosFragment
}