package com.dreampany.tensor.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Menu;
import android.view.View;

import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.injector.ActivityScoped;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.tensor.R;
import com.dreampany.tensor.data.model.Task;
import com.dreampany.tensor.databinding.FragmentEditTaskBinding;
import com.dreampany.tensor.ui.model.TaskItem;
import com.dreampany.tensor.ui.model.UiTask;
import com.dreampany.tensor.vm.EditTaskViewModel;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * Created by Hawladar Roman on 1/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@ActivityScoped
public class EditTaskFragment extends BaseMenuFragment
        implements View.OnClickListener {

    private FragmentEditTaskBinding binding;
    @Inject
    ViewModelProvider.Factory factory;
    @NonNull
    private EditTaskViewModel viewModel;

    @Inject
    public EditTaskFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_task;
    }

    @Override
    protected void onMenuCreated(Menu menu) {

    }

    @Override
    protected void onStartUi(Bundle state) {
        initView();
        UiTask<Task> uiTask = getCurrentTask(true);
        viewModel.setT(uiTask);
        viewModel.loadTitle();
        viewModel.loadTaskItem();
    }

    @Override
    protected void onStopUi() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                saveTask();
                break;
        }
    }

    private void initView() {
        binding = (FragmentEditTaskBinding) super.binding;
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this, factory).get(EditTaskViewModel.class);
        binding.fab.setOnClickListener(this);

        viewModel.getLiveTitle().observe(this, this::processTitle);
        viewModel.getLiveResponse().observe(this, this::processResponse);
    }

    private void saveTask() {
        viewModel.saveTask(binding.editTitle.getText().toString(), binding.editDescription.getText().toString());
    }

    private void processTitle(String title) {
        setTitle(title);
    }

    private void processResponse(Response<TaskItem> response) {
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
                        ViewUtil.showSnackbar(binding.editTitle, R.string.saved_task_message_successfully);
                        break;
                }
                AndroidUtil.hideSoftInput(getParent());
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

    private void updateUi(TaskItem item) {
        binding.editTitle.setText(item.getItem().getTitle());
        binding.editDescription.setText(item.getItem().getDescription());
    }


}
