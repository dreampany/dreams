package com.dreampany.tensor.vm;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.enums.Kind;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.rx.RxFacade;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.tensor.data.model.Task;
import com.dreampany.tensor.data.source.TaskRepository;
import com.dreampany.tensor.ui.model.TaskItem;
import com.dreampany.tensor.ui.model.UiTask;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 5/19/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class TaskViewModel extends BaseViewModel<UiTask<Task>> {

    @NonNull
    private final TaskRepository taskRepository;
    private Task task;
    private final MutableLiveData<Response<TaskItem>> response;

    @Inject
    public TaskViewModel(@NonNull Application application, @NonNull RxFacade facade, @NonNull TaskRepository taskRepository) {
        super(application, facade);
        this.taskRepository = taskRepository;
        response = new MutableLiveData<>();
        Timber.i("TaskRepository %s", taskRepository);
    }

    @Override
    protected Observable<String> getTitle() {
        return null;
    }

    @Override
    protected Observable<String> getSubtitle() {
        return null;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @NonNull
    public MutableLiveData<Response<TaskItem>> getResponse() {
        return response;
    }

    public void loadTaskItem() {
        Disposable disposable = getTaskItem()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                        item -> {
                            response.setValue(Response.success(Kind.READ, item));
                        }
                        , throwable -> Response.error(Kind.READ, throwable.getMessage()));
        disposables.add(disposable);
    }

    private Observable<TaskItem> getTaskItem() {
        if (isNewTask()) {
            return Observable.empty();
        }
        return taskRepository
                .getTask(task.getId())
                .map(this::restoreTask)
                .map(TaskItem::new);
    }

    private boolean isNewTask() {
        return task == null;
    }

    public void saveTask(String title, String description) {
        Completable completable = createTask(title, description);
        Disposable disposable = completable
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                        () -> {
                            response.setValue(Response.success(Kind.WRITE, null));
                        },
                        throwable -> {
                            response.setValue(Response.error(Kind.WRITE, throwable.getMessage()));
                        }
                );
        disposables.add(disposable);
    }

    private Task restoreTask(Task task) {
        String title = this.task.getTitle() != null ? this.task.getTitle() : task.getTitle();
        String description = this.task.getDescription() != null ? this.task.getDescription() : task.getDescription();
        return new Task(title, description);
    }

    private Completable createTask(String title, String description) {
        if (isNewTask()) {
            task = new Task(title, description);
            if (task.isEmpty()) {
                return Completable.complete();
            }
        } else {
            task.setTitle(title).setDescription(description);
            task.setTime(System.currentTimeMillis());
        }
        return taskRepository.saveTask(task);
    }


}
