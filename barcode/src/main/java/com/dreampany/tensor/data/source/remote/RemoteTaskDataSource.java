package com.dreampany.tensor.data.source.remote;

import androidx.annotation.NonNull;

import com.dreampany.tensor.data.model.Task;
import com.dreampany.tensor.data.source.TaskDataSource;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 */

@Singleton
public class RemoteTaskDataSource implements TaskDataSource {

    public RemoteTaskDataSource() {
    }

    @NonNull
    @Override
    public Observable<List<Task>> getTasks() {
        return null;
    }

    @NonNull
    @Override
    public Observable<Task> getTask(@NonNull String taskId) {
        return null;
    }

    @NonNull
    @Override
    public Completable saveTask(@NonNull Task task) {
        return Completable.complete();
    }

    @NonNull
    @Override
    public Completable saveTasks(@NonNull List<Task> tasks) {
        return Completable.complete();
    }

    @NonNull
    @Override
    public Completable completeTask(@NonNull Task task) {
        return null;
    }

    @NonNull
    @Override
    public Completable completeTask(@NonNull String taskId) {
        return null;
    }

    @Override
    public Completable activateTask(@NonNull Task task) {
        return null;
    }

    @Override
    public Completable activateTask(@NonNull String taskId) {
        return null;
    }

    @NonNull
    @Override
    public Completable clearCompletedTasks() {
        return null;
    }

    @NonNull
    @Override
    public Completable refreshTasks() {
        return null;
    }

    @NonNull
    @Override
    public Completable deleteAllTasks() {
        return null;
    }

    @NonNull
    @Override
    public Completable deleteTask(@NonNull String taskId) {
        return null;
    }
}
