package com.dreampany.lca.injector.app

import android.app.Application
import com.dreampany.frame.injector.app.AppModule
import com.dreampany.lca.app.App
import com.dreampany.lca.injector.data.BuildersModule
import com.dreampany.lca.injector.service.ServiceModule
import com.dreampany.lca.injector.ui.ActivityModule
import com.dreampany.lca.injector.worker.WorkerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 5/23/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
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