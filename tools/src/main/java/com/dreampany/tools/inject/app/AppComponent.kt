package com.dreampany.tools.inject.app

import android.app.Application
import com.dreampany.common.inject.app.AppModule
import com.dreampany.common.inject.property.PropertyModule
import com.dreampany.tools.inject.ui.activity.ActivityModule
import com.dreampany.tools.app.App
import com.dreampany.tools.inject.data.DataModule
import com.dreampany.tools.inject.service.ServiceModule
import com.dreampany.tools.inject.ui.vm.ViewModelModule
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
        ServiceModule::class,
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