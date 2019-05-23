package com.dreampany.lca.ui.activity;

import android.os.Bundle;

import com.dreampany.frame.data.model.Task;
import com.dreampany.frame.misc.SmartAd;
import com.dreampany.frame.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.lca.R;
import com.dreampany.lca.databinding.ActivityNavigationBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.fragment.CoinAlertsFragment;
import com.dreampany.lca.ui.fragment.CoinsFragment;
import com.dreampany.lca.ui.fragment.IcoFragment;
import com.dreampany.lca.ui.fragment.MoreFragment;
import com.dreampany.lca.ui.fragment.NewsFragment;
import com.dreampany.lca.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NavigationActivity
        extends BaseBottomNavigationActivity {

    @Inject
    Lazy<CoinsFragment> coinsFragment;
    @Inject
    Lazy<CoinAlertsFragment> coinAlertsFragment;
    @Inject
    Lazy<IcoFragment> icoFragment;
    @Inject
    Lazy<NewsFragment> newsFragment;
    @Inject
    Lazy<MoreFragment> moreFragment;
    @Inject
    SmartAd ad;
    ActivityNavigationBinding binding;

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
        return R.id.item_coins;
    }

    @Override
    public boolean isHomeUp() {
        return false;
    }

    @Override
    public boolean hasRatePermitted() {
        return true;
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
        UiTask<?> uiTask = getCurrentTask(false);
        if (uiTask != null && uiTask.getType() != null && uiTask.getSubtype() != null) {
            openActivity(ToolsActivity.class, uiTask);
            return;
        }

        binding = (ActivityNavigationBinding) super.binding;
        ad.initAd(this,
                getClass().getSimpleName(),
                findViewById(R.id.adview),
                R.string.interstitial_ad_unit_id,
                R.string.rewarded_ad_unit_id);
        ad.loadBanner(getClass().getSimpleName());
    }

    @Override
    protected void onStopUi() {
        ad.destroyBanner(getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.resumeBanner(getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        ad.pauseBanner(getClass().getSimpleName());
        super.onPause();
    }

/*    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();
        if (fragment != null && fragment.hasBackPressed()) {
            return;
        }
        finish();
    }*/

    @Override
    protected void onNavigationItem(int navigationItemId) {
        switch (navigationItemId) {
            case R.id.item_coins:
                commitFragment(CoinsFragment.class, coinsFragment, R.id.layout);
                break;
            case R.id.item_alerts:
                commitFragment(CoinAlertsFragment.class, coinAlertsFragment, R.id.layout);
                break;
            case R.id.item_ico:
                commitFragment(IcoFragment.class, icoFragment, R.id.layout);
                break;
            case R.id.item_news:
                commitFragment(NewsFragment.class, newsFragment, R.id.layout);
                break;
            case R.id.item_more:
                commitFragment(MoreFragment.class, moreFragment, R.id.layout);
                break;
        }
    }

    @Override
    public void execute(@NotNull Task<?> t) {
        setSelectedItem(R.id.item_coins);
    }
}
