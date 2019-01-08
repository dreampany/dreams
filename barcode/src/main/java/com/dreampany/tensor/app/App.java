package com.dreampany.tensor.app;

import com.dreampany.frame.app.BaseApp;
import com.dreampany.tensor.BuildConfig;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class App extends BaseApp {

    @Override
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return null;// DaggerAppComponent.builder().application(this).build();
    }
}
