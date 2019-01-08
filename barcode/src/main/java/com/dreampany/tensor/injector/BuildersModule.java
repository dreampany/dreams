package com.dreampany.tensor.injector;

import android.app.Application;

import com.dreampany.tensor.data.source.Local;
import com.dreampany.tensor.data.source.Remote;
import com.dreampany.tensor.data.source.TaskDataSource;
import com.dreampany.tensor.data.source.local.DatabaseManager;
import com.dreampany.tensor.data.source.local.LocalTaskDataSource;
import com.dreampany.tensor.data.source.local.TaskDao;
import com.dreampany.tensor.data.source.remote.RemoteTaskDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hawladar Roman on 5/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module(includes = ViewModelModule.class)
class BuildersModule {

/*    @Singleton
    @Provides
    RxFacade provideRxFacade() {
        return new RxFacade();
    }*/

    @Singleton
    @Provides
    @Local
    TaskDataSource provideLocalTasksDataSource(TaskDao taskDao) {
        return new LocalTaskDataSource(taskDao);
    }

    @Singleton
    @Provides
    @Remote
    TaskDataSource provideRemoteTasksDataSource() {
        return new RemoteTaskDataSource();
    }

    @Singleton
    @Provides
    DatabaseManager provideDatabase(Application application) {
        return DatabaseManager.onInstance(application.getApplicationContext());
    }

    @Singleton
    @Provides
    TaskDao provideTaskDao(DatabaseManager database) {
        return database.taskDao();
    }

/*    @Singleton
    @Provides
    AppExecutors provideExecutors() {
        return new AppExecutors();
    }*/
}
