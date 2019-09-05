package com.dreampany.cast.ui.activity;

import android.os.Bundle;

import com.dreampany.cast.R;
import com.dreampany.cast.databinding.ActivityNavigationBinding;
import com.dreampany.cast.misc.Constants;
import com.dreampany.cast.ui.fragment.HomeFragment;
import com.dreampany.cast.ui.fragment.MoreFragment;
import com.dreampany.cast.ui.model.UiTask;
import com.dreampany.framework.misc.SmartAd;
import com.dreampany.framework.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.framework.ui.fragment.BaseFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NavigationActivity extends BaseBottomNavigationActivity {

    @Inject
    Lazy<MoreFragment> moreFragment;
    @Inject
    Lazy<HomeFragment> homeFragment;
    @Inject
    SmartAd ad;

    private ActivityNavigationBinding bind;

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
    public boolean hasDoubleBackPressed() {
        return true;
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Screen.navigation(getApplicationContext());
    }

    @Override
    protected void onStartUi(Bundle state) {
        initView();
        ad.loadBanner(getClass().getSimpleName());
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
                commitFragment(HomeFragment.class, homeFragment, R.id.layout);
                break;
            case R.id.item_more:
                commitFragment(MoreFragment.class, moreFragment, R.id.layout);
                break;
        }
    }

    @Override
    protected void onStopUi() {

    }

    private void initView() {
        UiTask<?> uiTask = getCurrentTask(false);
        if (uiTask != null && uiTask.getType() != null && uiTask.getSubtype() != null) {
            openActivity(ToolsActivity.class, uiTask);
            return;
        }

        bind = (ActivityNavigationBinding) super.binding;
        ad.initAd(this,
                getClass().getSimpleName(),
                findViewById(R.id.adview),
                R.string.interstitial_ad_unit_id,
                R.string.rewarded_ad_unit_id);
    }
}
