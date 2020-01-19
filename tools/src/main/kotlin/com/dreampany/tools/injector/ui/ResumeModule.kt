package com.dreampany.tools.injector.ui

import com.dreampany.framework.misc.FragmentScope
import com.dreampany.tools.ui.fragment.resume.ResumeFragment
import com.dreampany.tools.ui.fragment.resume.ResumeHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract
class ResumeModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): ResumeHomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun resumeFragment(): ResumeFragment
}