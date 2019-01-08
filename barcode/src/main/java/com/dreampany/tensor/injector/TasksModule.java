package com.dreampany.tensor.injector;

import com.dreampany.frame.injector.FragmentScoped;
import com.dreampany.tensor.ui.fragment.TasksFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class TasksModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract TasksFragment taskFragment();
}
