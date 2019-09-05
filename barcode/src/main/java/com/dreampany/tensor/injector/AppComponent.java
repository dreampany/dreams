package com.dreampany.tensor.injector;


import android.app.Application;

import com.dreampany.framework.injector.AppModule;
import com.dreampany.tensor.app.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BuildersModule.class,
        ActivityModule.class,
})
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
