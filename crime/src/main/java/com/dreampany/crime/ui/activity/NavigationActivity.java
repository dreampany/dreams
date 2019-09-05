package com.dreampany.crime.ui.activity;

import android.os.Bundle;

import com.dreampany.crime.ui.fragment.HomeFragment;
import com.dreampany.framework.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.crime.R;
import com.dreampany.crime.ui.fragment.MoreFragment;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NavigationActivity extends BaseBottomNavigationActivity {

    @Inject
    Lazy<HomeFragment> homeProvider;
    @Inject
    Lazy<MoreFragment> moreProvider;

    @Override
    public int getLayoutId() {
        return R.layout.activity_navigation;
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public int getNavigationViewId() {
        return R.id.navigation_view;
    }

    @Override
    public int getDefaultSelectedNavigationItemId() {
        return R.id.item_home;
    }

    @Override
    public boolean isHomeUp() {
        return false;
    }

    @Override
    protected void onStartUi(Bundle state) {

    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();
        if (fragment != null && fragment.hasBackPressed()) {
            return;
        }
        finish();
    }

    @Override
    protected void onNavigationItem(int navigationItemId) {
        switch (navigationItemId) {
            case R.id.item_home:
                commitFragment(HomeFragment.class, homeProvider, R.id.layout);
                break;
            case R.id.item_more:
                commitFragment(MoreFragment.class, moreProvider, R.id.layout);
                break;
        }
    }

    @Override
    protected void onStopUi() {

    }
}
