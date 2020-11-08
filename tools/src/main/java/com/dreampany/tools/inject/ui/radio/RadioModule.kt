package com.dreampany.tools.inject.ui.radio

import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.tools.ui.radio.activity.PagesActivity
import com.dreampany.tools.ui.radio.activity.RadioActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class RadioModule {

    @ContributesAndroidInjector
    abstract fun pages(): PagesActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [StationsModule::class])
    abstract fun radio(): RadioActivity
}