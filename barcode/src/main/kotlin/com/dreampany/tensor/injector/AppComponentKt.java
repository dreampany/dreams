package com.dreampany.tensor.injector;


import android.app.Application;

import com.dreampany.frame.injector.AppModule;
import com.dreampany.tensor.app.AppKt;

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
public interface AppComponentKt extends AndroidInjector<AppKt> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponentKt build();
    }
}
