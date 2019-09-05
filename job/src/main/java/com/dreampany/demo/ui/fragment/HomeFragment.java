package com.dreampany.quran.ui.fragment;

import android.os.Bundle;

import com.dreampany.quran.R;
import com.dreampany.quran.ui.model.UiTask;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.fragment.BaseStateFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;


/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseStateFragment<BaseFragment> {

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpager_fixed;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_home;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @NonNull
    @Override
    protected String[] pageTitles() {
        return new String[]{"","", ""};
    }

    @NonNull
    @Override
    protected Class<BaseFragment>[] pageClasses() {
        return new Class[]{FirstFragment.class, SecondFragment.class, ThirdFragment.class};
    }

    @NotNull
    @Override
    protected Task<?>[] pageTasks() {
        UiTask<?> task = getCurrentTask(false);
        return new Task[]{task, task, task};
    }

    @Override
    public boolean hasAllPages() {
        return true;
    }

    @Override
    public boolean hasTabColor() {
        return true;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
    }

    @Override
    protected void onStopUi() {

    }


    @NonNull
    @Override
    public ViewModelProvider.Factory get() {
        return factory;
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
    }

    private void initView() {
        setTitle(R.string.home);
    }
}
