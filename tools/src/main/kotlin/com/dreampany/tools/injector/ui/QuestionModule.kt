package com.dreampany.tools.injector.ui

import com.dreampany.framework.misc.FragmentScope
import com.dreampany.tools.ui.fragment.question.QuestionHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class QuestionModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): QuestionHomeFragment
}