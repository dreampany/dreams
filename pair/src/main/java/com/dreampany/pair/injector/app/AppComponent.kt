package com.dreampany.pair.injector.app

import android.app.Application
import com.dreampany.common.injector.app.AppModule
import com.dreampany.common.injector.property.PropertyModule
import com.dreampany.pair.app.App
import com.dreampany.pair.injector.data.DataModule
import com.dreampany.pair.injector.ui.ActivityModule
import com.dreampany.pair.injector.ui.vm.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        PropertyModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        DataModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}