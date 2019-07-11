package com.dreampany.translate.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.dreampany.frame.misc.SmartAd;
import com.dreampany.frame.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.translate.R;
import com.dreampany.translate.databinding.ActivityNavigationBinding;
import com.dreampany.translate.misc.Constants;
import com.dreampany.translate.ui.fragment.HomeFragment;
import com.dreampany.translate.ui.fragment.MoreFragment;
import com.dreampany.translate.ui.model.UiTask;

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
    ViewModelProvider.Factory factory;
    @Inject
    SmartAd ad;

  private   ActivityNavigationBinding bind;

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

    @NonNull
    @Override
    public String getScreen() {
        return Constants.Companion.navigation(getApplicationContext());
    }

    @Override
    protected void onStartUi(Bundle state) {
        initView();
        ad.loadBanner(getScreen());
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
                getScreen(),
                findViewById(R.id.adview),
                R.string.interstitial_ad_unit_id,
                R.string.rewarded_ad_unit_id);
    }
}
