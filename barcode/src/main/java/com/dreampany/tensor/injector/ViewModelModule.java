package com.dreampany.tensor.injector;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dreampany.framework.injector.ViewModelKey;
import com.dreampany.tensor.factory.TaskViewModelFactory;
import com.dreampany.tensor.vm.EditTaskViewModel;
import com.dreampany.tensor.vm.TaskViewModel;
import com.dreampany.tensor.vm.TasksViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Hawladar Roman on 5/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TasksViewModel.class)
    abstract ViewModel bindTasksViewModel(TasksViewModel tasksViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditTaskViewModel.class)
    abstract ViewModel bindEditTaskViewModel(EditTaskViewModel editTaskViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel.class)
    abstract ViewModel bindTaskViewModel(TaskViewModel taskViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(TaskViewModelFactory factory);
}
