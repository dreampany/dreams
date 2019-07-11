package com.dreampany.translate.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.translate.R;

import javax.inject.Inject;


/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseMenuFragment {

    private final String NONE = "none";
    private final String SEARCH = "search";
    private final String EMPTY = "empty";

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
