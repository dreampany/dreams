package com.dreampany.tensor.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.injector.ActivityScoped;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.tensor.R;
import com.dreampany.tensor.data.model.Task;
import com.dreampany.tensor.databinding.FragmentTasksBinding;
import com.dreampany.tensor.ui.activity.ToolsActivity;
import com.dreampany.tensor.ui.adapter.TaskAdapter;
import com.dreampany.tensor.ui.enums.UiSubtype;
import com.dreampany.tensor.ui.enums.UiType;
import com.dreampany.tensor.ui.model.TaskItem;
import com.dreampany.tensor.ui.model.UiTask;
import com.dreampany.tensor.vm.TasksViewModel;
import com.yinglan.keyboard.HideUtil;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import timber.log.Timber;

@ActivityScoped
public class TasksFragment extends BaseMenuFragment implements
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = TasksFragment.class.getSimpleName();

    private FragmentTasksBinding binding;
    private TaskAdapter adapter;
    private final int offset = 4;

    @Inject
    ViewModelProvider.Factory factory;
    @NonNull
    private TasksViewModel viewModel;

    @Inject
    public TasksFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tasks;
    }

    @Override
    protected int getMenuId() {
        return R.menu.menu_fragment_tasks;
    }

    @Override
    protected void onStartUi(Bundle state) {
        initView();
        initRecycler();
        UiTask<Task> uiTask = getCurrentTask(true);
        viewModel.setT(uiTask);
        viewModel.loadTitle();
        viewModel.loadTaskItems();
    }

    @Override
    protected void onMenuCreated(Menu menu) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filter:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                viewModel.addNewTask();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //presenter.result(requestCode, resultCode);
    }

    @Override
    public void onRefresh() {

    }

    private void initView() {
        binding = (FragmentTasksBinding) super.binding;
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this, factory).get(TasksViewModel.class);
        binding.fab.setOnClickListener(this);
        ViewUtil.setSwipe(binding.swipeRefresh, this);
        viewModel.getLiveNewTaskEvent().observe(this, this::openAddTaskUi);
        viewModel.getLiveResponse().observe(this, this::processResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new TaskAdapter((FlexibleAdapter.OnItemClickListener) (view, position) -> {
            if (position != RecyclerView.NO_POSITION) {
                TaskItem item = adapter.getItem(position);
                openEditTaskUi(item.getItem());
                return true;
            }
            return false;
        });

        ViewUtil.setRecycler(
                binding.recyclerView,
                adapter,
                new SmoothScrollLinearLayoutManager(getContext()),
                null,
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_task, offset)
                        .withEdge(true)
        );
    }

    private void openAddTaskUi(Void param) {
        openEditTaskUi(null);
    }

    private void openEditTaskUi(Task item) {
        UiTask<Task> task = new UiTask<>(false);
        task.setInput(item);
        task.setUiType(UiType.TASK);
        task.setSubtype(UiSubtype.EDIT);
        openActivityParcelable(ToolsActivity.class, task);
    }

    private void processResponse(Response<List<TaskItem>> response) {
        switch (response.status) {
            case READING:
                binding.stateful.showProgress();
                Timber.v("READING");
                break;

            case SUCCESS:
                binding.stateful.showContent();
                switch (response.kind) {
                    case READ:
                        updateUi(response.data);
                        break;
                    case WRITE:
                        //ViewUtil.showSnackbar(binding.editTitle, R.string.saved_task_message_successfully);
                        break;
                }
                HideUtil.hideSoftKeyboard(getParent());
                Timber.v("SUCCESS");
                break;

            case ERROR:
                binding.stateful.showEmpty();
                Timber.v("ERROR");
                break;

            case EMPTY:
                binding.stateful.showEmpty();
                Timber.v("EMPTY");
                break;
        }
    }

    private void updateUi(List<TaskItem> items) {
        adapter.clear();
        adapter.addItems(0, items);
    }
}
