package com.dreampany.tensor.data.source;

import android.support.annotation.NonNull;

import com.dreampany.tensor.data.model.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface TaskDataSource {

    @NonNull
    Observable<List<Task>> getTasks();

    @NonNull
    Observable<Task> getTask(@NonNull String taskId);

    @NonNull
    Completable saveTask(@NonNull Task task);

    @NonNull
    Completable saveTasks(@NonNull List<Task> tasks);

    @NonNull
    Completable completeTask(@NonNull Task task);

    @NonNull
    Completable completeTask(@NonNull String taskId);

    Completable activateTask(@NonNull Task task);

    Completable activateTask(@NonNull String taskId);

    @NonNull
    Completable clearCompletedTasks();

    @NonNull
    Completable refreshTasks();

    @NonNull
    Completable deleteAllTasks();

    @NonNull
    Completable deleteTask(@NonNull String taskId);
}
