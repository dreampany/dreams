package com.dreampany.tensor.vm;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.dreampany.frame.data.enums.Kind;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.ld.SingleLiveEvent;
import com.dreampany.frame.rx.RxFacade;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.tensor.R;
import com.dreampany.tensor.data.enums.Filter;
import com.dreampany.tensor.data.model.Task;
import com.dreampany.tensor.data.source.TaskRepository;
import com.dreampany.tensor.ui.model.TaskItem;
import com.dreampany.tensor.ui.model.UiTask;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;


/**
 * Created by Hawladar Roman on 5/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

public final class TasksViewModel extends BaseViewModel<UiTask<Task>> {

    private static final String KEY_FILTER = "filter";
    @NonNull
    private final TaskRepository taskRepository;
    private final MutableLiveData<Response<List<TaskItem>>> liveResponse;
    @NonNull
    private final BehaviorSubject<Filter> filter;
    @NonNull
    private final SingleLiveEvent<Void> liveNewTaskEvent;

    @Inject
    public TasksViewModel(@NonNull Application application, @NonNull RxFacade facade, @NonNull TaskRepository taskRepository) {
        super(application, facade);
        this.taskRepository = taskRepository;
        liveResponse = new MutableLiveData<>();
        filter = BehaviorSubject.createDefault(Filter.ALL);
        liveNewTaskEvent = new SingleLiveEvent<>();
    }

    @Override
    protected Observable<String> getTitle() {
        return Observable.fromCallable(() -> {
            int resourceId = R.string.tasks;
            return TextUtil.getString(getApplication(), resourceId);
        });
    }

    @Override
    protected Observable<String> getSubtitle() {
        return Observable.empty();
    }

    public MutableLiveData<Response<List<TaskItem>>> getLiveResponse() {
        return liveResponse;
    }

    @NonNull
    public SingleLiveEvent<Void> getLiveNewTaskEvent() {
        return liveNewTaskEvent;
    }

    public void loadTaskItems() {
        Disposable disposable = getTaskItems()
                .onErrorReturn(throwable -> Response.error(Kind.READ, throwable.getMessage()))
                .startWith(Response.reading(Kind.READ))
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                        liveResponse::setValue,
                        throwable -> Response.error(Kind.READ, throwable.getMessage()));
        disposables.add(disposable);
    }

    @NonNull
    public void addNewTask() {
        liveNewTaskEvent.call();
    }

    public void handleActivityResult(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (/*AddEditTaskActivity.REQUEST_ADD_TASK == requestCode &&*/ Activity.RESULT_OK == resultCode) {
            // snackbarMessage.onNext(R.string.successfully_saved_task_message);
        }
    }

/*    @NonNull
    private Boolean shouldFilterTask(Task task, Filter filter) {
        switch (filter) {
            case ACTIVE:
                return task.isActive();
            case COMPLETED:
                return task.isCompleted();
            case ALL:
            default:
                return true;
        }
    }*/

    @StringRes
    public int getFilterRes(Filter filter) {
        switch (filter) {
            case ACTIVE:
                return R.string.label_active;
            case COMPLETED:
                return R.string.label_completed;
            case ALL:
            default:
                return R.string.label_all;
        }
    }

    public void setFilter(Filter filter) {
        this.filter.onNext(filter);
    }

    public void restoreState(@Nullable Bundle bundle) {
        if (bundle != null && bundle.containsKey(KEY_FILTER)) {
            Filter filter = (Filter) bundle.getSerializable(KEY_FILTER);
            this.filter.onNext(filter);
        }
    }

    @NonNull
    public Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FILTER, filter.getValue());
        return bundle;
    }

    @NonNull
    public Completable clearCompletedTasks() {
        return Completable.fromAction(this::clearCompletedTasksAndNotify);
    }

    private void clearCompletedTasksAndNotify() {
        taskRepository.clearCompletedTasks();
        //snackbarMessage.onNext(R.string.completed_tasks_cleared);
    }

    private Observable<Response<List<TaskItem>>> getTaskItems() {
        return taskRepository.getTasks()
                .flatMap(tasks ->
                        Observable.fromIterable(tasks).map(TaskItem::new).toList().toObservable()
                ).map(items -> Response.success(Kind.READ, items));
    }
}
