package com.dreampany.translate.injector.app

import android.app.Application
import com.dreampany.translate.app.App
import com.dreampany.translate.injector.data.BuildersModule
import com.dreampany.translate.injector.service.ServiceModule
import com.dreampany.translate.injector.ui.ActivityModule
import com.dreampany.translate.injector.worker.WorkerModule
import com.dreampany.frame.injector.app.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    ServiceModule::class,
    WorkerModule::class,
    BuildersModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}