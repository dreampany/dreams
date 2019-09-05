package com.dreampany.crime.ui.fragment;

import android.app.Activity;
import android.app.SearchManager;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.fragment.BaseStateFragment;
import com.dreampany.crime.R;
import com.dreampany.crime.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;



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
    protected void onMenuCreated(@NonNull Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_FULLSCREEN);
            searchView.setQueryHint(getString(R.string.search));
            SearchManager searchManager = (SearchManager) searchView.getContext().getSystemService(Context.SEARCH_SERVICE);
            Activity activity = getActivity();
            if (searchManager != null && activity != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
                searchView.setOnQueryTextListener(this);
            }
        }
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
