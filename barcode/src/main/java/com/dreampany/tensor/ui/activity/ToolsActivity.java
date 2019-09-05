package com.dreampany.tensor.ui.activity;

import android.os.Bundle;

import com.dreampany.framework.ui.activity.BaseMenuActivity;
import com.dreampany.tensor.R;
import com.dreampany.tensor.ui.enums.UiSubtype;
import com.dreampany.tensor.ui.enums.UiType;
import com.dreampany.tensor.ui.fragment.EditTaskFragment;
import com.dreampany.tensor.ui.fragment.TaskFragment;
import com.dreampany.tensor.ui.model.UiTask;

import javax.inject.Inject;

import dagger.Lazy;


/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */


public class ToolsActivity extends BaseMenuActivity {

    @Inject
    Lazy<EditTaskFragment> editTaskFragmentProvider;
    @Inject
    Lazy<TaskFragment> taskFragmentProvider;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tools;
    }

    @Override
    protected boolean isFullScreen() {
        UiTask task = (UiTask) getCurrentTask(true);
        if (task != null) {
            return task.isFullscreen();
        }
        return super.isFullScreen();
    }

    @Override
    protected void onStartUi(Bundle state) {
        UiTask task = (UiTask) getCurrentTask(false);
        if (task == null) {
            return;
        }

        UiType type = task.getType();
        UiSubtype subtype = task.getSubtype();

        if (type == null || subtype == null) {
            return;
        }

        switch (type) {
            case TASK: {
                switch (subtype) {
                    case EDIT: {
                        commitFragment(EditTaskFragment.class, editTaskFragmentProvider, R.id.layout, task);
                    }
                    break;
                    case VIEW: {
                        commitFragment(TaskFragment.class, taskFragmentProvider, R.id.layout, task);
                    }
                    break;
                }
            }
            break;
        }
    }

    @Override
    protected void onStopUi() {

    }
}
