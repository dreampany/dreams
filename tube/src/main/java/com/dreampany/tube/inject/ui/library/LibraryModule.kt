package com.dreampany.tube.inject.ui.library

import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.tube.inject.ui.home.VideoModule
import com.dreampany.tube.ui.home.fragment.LibraryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 11/29/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class LibraryModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [VideoModule::class])
    abstract fun library(): LibraryFragment
}