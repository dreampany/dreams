package com.dreampany.fit.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.dreampany.fit.R;
import com.dreampany.fit.misc.Constants;
import com.dreampany.fit.util.Util;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_home;
    }

/*    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }*/

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
    }

    @Override
    public void onPermissionGranted(@Nullable PermissionGrantedResponse response) {
        //TODO action to start listening sensors events using AppService
        Util.listenStepCounter();
    }

    @Override
    public void onPermissionDenied(@Nullable PermissionDeniedResponse response) {
        //TODO show a perfect message to user
    }

    private void initView() {
        setTitle(R.string.home);

        Dexter.withActivity(getParent())
                .withPermission(Constants.Permission.Location)
                .withListener(this)
                .check();
    }
}
