package com.dreampany.tensor.injector;

import com.dreampany.framework.injector.ActivityScoped;
import com.dreampany.tensor.ui.activity.LaunchActivityKt;
import com.dreampany.tensor.ui.activity.NavigationActivity;
import com.dreampany.tensor.ui.activity.ToolsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityModule {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract LaunchActivityKt launchActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {TasksModule.class, MoreModule.class})
    abstract NavigationActivity navigationActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {EditTaskModule.class, TaskModule.class})
    abstract ToolsActivity toolsActivity();
}
