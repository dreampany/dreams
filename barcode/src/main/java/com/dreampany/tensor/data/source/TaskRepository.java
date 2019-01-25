package com.dreampany.tensor.data.source;


import androidx.annotation.NonNull;

import com.dreampany.tensor.data.model.Task;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Singleton
public class TaskRepository implements TaskDataSource {

    private final TaskDataSource localDataSource;
    private final TaskDataSource remoteDataSource;

    private Map<String, Task> cachedTasks;
    private boolean cacheIsDirty = false;

    @Inject
    public TaskRepository(@Local TaskDataSource localDataSource, @Remote TaskDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @NonNull
    @Override
    public Observable<List<Task>> getTasks() {
        return localDataSource.getTasks();
    }

    @NonNull
    @Override
    public Observable<Task> getTask(@NonNull String taskId) {
        Preconditions.checkNotNull(taskId);
        return localDataSource.getTask(taskId);
    }

    @NonNull
    @Override
    public Completable saveTask(@NonNull Task task) {
        Preconditions.checkNotNull(task);
        return localDataSource.saveTask(task).andThen(remoteDataSource.saveTask(task));
    }

    @NonNull
    @Override
    public Completable saveTasks(@NonNull List<Task> tasks) {
        Preconditions.checkNotNull(tasks);
        return localDataSource.saveTasks(tasks).andThen(remoteDataSource.saveTasks(tasks));
    }

    @NonNull
    @Override
    public Completable completeTask(@NonNull Task task) {
        Preconditions.checkNotNull(task);
        return localDataSource.completeTask(task).andThen(remoteDataSource.completeTask(task));
    }

    @NonNull
    @Override
    public Completable completeTask(@NonNull String taskId) {
        Preconditions.checkNotNull(taskId);
        return localDataSource.completeTask(taskId).andThen(remoteDataSource.completeTask(taskId));
    }

    @NonNull
    @Override
    public Completable activateTask(@NonNull Task task) {
        Preconditions.checkNotNull(task);
        return localDataSource.activateTask(task).andThen(remoteDataSource.activateTask(task));
    }

    @NonNull
    @Override
    public Completable activateTask(@NonNull String taskId) {
        Preconditions.checkNotNull(taskId);
        return localDataSource.activateTask(taskId).andThen(remoteDataSource.activateTask(taskId));
    }

    @NonNull
    @Override
    public Completable clearCompletedTasks() {
        return localDataSource.clearCompletedTasks().andThen(remoteDataSource.clearCompletedTasks());
    }

    @NonNull
    @Override
    public Completable refreshTasks() {
        return localDataSource.refreshTasks().andThen(remoteDataSource.refreshTasks());
    }

    @NonNull
    @Override
    public Completable deleteAllTasks() {
        return localDataSource.deleteAllTasks().andThen(remoteDataSource.deleteAllTasks());
    }

    @NonNull
    @Override
    public Completable deleteTask(@NonNull String taskId) {
        return localDataSource.deleteTask(taskId).andThen(remoteDataSource.deleteTask(taskId));
    }
}