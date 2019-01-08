package com.dreampany.tensor.vm;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dreampany.frame.data.enums.Kind;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.rx.RxFacade;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.tensor.R;
import com.dreampany.tensor.data.model.Task;
import com.dreampany.tensor.data.source.TaskRepository;
import com.dreampany.tensor.ui.model.TaskItem;
import com.dreampany.tensor.ui.model.UiTask;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 5/19/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class EditTaskViewModel extends BaseViewModel<UiTask<Task>> {

    @NonNull
    private final TaskRepository taskRepository;
    @NonNull
    private final MutableLiveData<Response<TaskItem>> liveResponse;

    private Task pendingTask;

    @Inject
    EditTaskViewModel(@NonNull Application application, @NonNull RxFacade facade, @NonNull TaskRepository taskRepository) {
        super(application, facade);
        this.taskRepository = taskRepository;
        liveResponse = new MutableLiveData<>();
    }

    @Override
    protected Observable<String> getTitle() {
        return Observable.fromCallable(() -> {
            Task task = getTask();
            int resourceId = task == null ? R.string.add_task : R.string.edit_task;
            return TextUtil.getString(getApplication(), resourceId);
        });
    }

    @Override
    protected Observable<String> getSubtitle() {
        return Observable.empty();
    }

    @NonNull
    public MutableLiveData<Response<TaskItem>> getLiveResponse() {
        return liveResponse;
    }

    public void loadTaskItem() {
        Disposable disposable = getTaskItem()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                        item -> {
                            liveResponse.setValue(Response.success(Kind.READ, item));
                        }
                        , throwable -> liveResponse.setValue(Response.error(Kind.READ, throwable.getMessage())));
        addSubscription(disposable);
    }

    private Observable<TaskItem> getTaskItem() {
        Task task = getTask();
        if (task != null) {
            return taskRepository
                    .getTask(task.getId())
                    //.map(this::restoreTask)
                    .map(TaskItem::new);
        }
        return Observable.empty();
    }

    public void saveTask(String title, String description) {
        Disposable disposable = createTask(title, description)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .doOnSubscribe(d -> liveResponse.setValue(Response.reading(Kind.WRITE)))
                .subscribe(
                        () -> {
                            cacheTask(pendingTask);
                            pendingTask = null;
                            loadTitle();
                            liveResponse.setValue(Response.success(Kind.WRITE, null));
                        },
                        throwable -> {
                            liveResponse.setValue(Response.error(Kind.WRITE, throwable.getMessage()));
                        }
                );
        addSubscription(disposable);
    }

    private Completable createTask(String title, String description) {
        if (!hasTask()) {
            pendingTask = new Task(title, description);
            if (pendingTask.isEmpty()) {
                pendingTask = null;
                return Completable.complete();
            }
        } else {
            pendingTask = getTask();
            pendingTask.setTitle(title).setDescription(description);
            pendingTask.setTime(System.currentTimeMillis());
        }
        return taskRepository.saveTask(pendingTask);
    }

    private boolean hasTask() {
        Task task = getTask();
        return (task != null);
    }

    private void cacheTask(Task task) {
        UiTask<Task> uiTask = getT();
        if (uiTask != null) {
            uiTask.setInput(task);
        }
    }

    private Task getTask() {
        UiTask<Task> uiTask = getT();
        if (uiTask != null) {
            return uiTask.getInput();
        }
        return null;
    }
}
