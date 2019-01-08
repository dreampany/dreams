package com.dreampany.tensor.ui.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Menu;
import android.view.View;

import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.injector.ActivityScoped;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.tensor.R;
import com.dreampany.tensor.ui.model.TaskItem;
import com.dreampany.tensor.vm.TasksViewModel;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * Created by Hawladar Roman on 1/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@ActivityScoped
public class TaskFragment extends BaseMenuFragment
        implements View.OnClickListener {

   // private FragmentTaskBinding binding;
    @Inject
    ViewModelProvider.Factory factory;
    @NonNull
    private TasksViewModel viewModel;


    @Inject
    public TaskFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void onMenuCreated(Menu menu) {

    }

    @Override
    protected void onStartUi(Bundle state) {
        setTitle(R.string.home);
        initView();
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
       // binding = (FragmentTaskBinding) super.binding;
/*        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this, factory).get(TasksViewModel.class);
        Timber.i("EditTaskViewModel - %s", viewModel);
        binding.fab.setOnClickListener(this);

        viewModel.getLiveResponse().observe(this, this::processResponse);*/
    }

    private void saveTask() {
       // viewModel.saveTask(binding.editTitle.getText().toString(), binding.editDescription.getText().toString());
    }

    private void processResponse(Response<TaskItem> response) {
        switch (response.status) {
            case READING:
                //renderLoadingState();
                Timber.i("READING");
                break;

            case SUCCESS:
                //renderDataState(response.data);
                Timber.i("SUCCESS");
                break;

            case ERROR:
                //renderErrorState(response.error);
                Timber.i("ERROR");
                break;

            case EMPTY:
                //renderErrorState(response.error);
                Timber.i("EMPTY");
                break;
        }
    }

    private void updateUi(TaskItem item) {
        //binding.editTitle.setText(item.getItem().getTitle());
       // binding.editDescription.setText(item.getItem().getDescription());
    }

    private void showSnackbar(@StringRes int textId) {
        //Snackbar.make(binding.editTitle, textId, Snackbar.LENGTH_LONG).show();
    }

}
