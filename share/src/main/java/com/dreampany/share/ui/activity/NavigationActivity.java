package com.dreampany.share.ui.activity;

import android.os.Bundle;

import com.dreampany.frame.misc.SmartAd;
import com.dreampany.share.databinding.ActivityNavigationBinding;
import com.dreampany.share.ui.fragment.DiscoverFragment;
import com.dreampany.frame.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.share.R;
import com.dreampany.share.ui.fragment.DownloadFragment;
import com.dreampany.share.ui.fragment.MoreFragment;
import com.dreampany.share.ui.fragment.ShareFragment;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NavigationActivity extends BaseBottomNavigationActivity {

    @Inject
    Lazy<DiscoverFragment> discoverProvider;
    @Inject
    Lazy<ShareFragment> shareProvider;
    @Inject
    Lazy<DownloadFragment> downloadProvider;
    @Inject
    Lazy<MoreFragment> moreProvider;
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
        return R.id.item_discover;
    }

    @Override
    public boolean isHomeUp() {
        return false;
    }

    @Override
    protected void onStartUi(Bundle state) {
        binding = (ActivityNavigationBinding) super.binding;
        ad.loadBanner(findViewById(R.id.adview));
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
            case R.id.item_discover:
                commitFragment(DiscoverFragment.class, discoverProvider, R.id.layout);
                break;
            case R.id.item_share:
                commitFragment(ShareFragment.class, shareProvider, R.id.layout);
                break;
            case R.id.item_download:
                commitFragment(DownloadFragment.class, downloadProvider, R.id.layout);
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
