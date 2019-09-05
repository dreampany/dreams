package com.dreampany.tensor.injector;

import com.dreampany.framework.injector.FragmentScoped;
import com.dreampany.tensor.ui.fragment.TaskFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Hawladar Roman on 1/5/18.
 * Dreampany
 * dreampanymail@gmail.com
 */

@Module
public abstract class TaskModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract TaskFragment taskFragment();
}
