package com.dreampany.word.ui.activity;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.dreampany.frame.misc.SmartAd;
import com.dreampany.frame.ui.activity.BaseActivity;
import com.dreampany.frame.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.frame.ui.callback.SearchViewCallback;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.word.R;
import com.dreampany.word.databinding.ActivityNavigationBinding;
import com.dreampany.word.ui.fragment.FlagFragment;
import com.dreampany.word.ui.fragment.HomeFragment;
import com.dreampany.word.ui.fragment.MoreFragment;
import com.dreampany.word.ui.fragment.OcrFragment;
import com.dreampany.word.ui.fragment.RecentFragment;
import com.dreampany.word.ui.fragment.SearchFragment;
import com.dreampany.word.vm.LoaderViewModel;

import com.lapism.searchview.widget.SearchView;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NavigationActivity extends BaseBottomNavigationActivity implements SearchViewCallback {

    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    SmartAd ad;
    @Inject
    Lazy<MoreFragment> moreFragment;
    @Inject
    Lazy<RecentFragment> recentFragment;
    @Inject
    Lazy<HomeFragment> homeFragment;
    @Inject
    Lazy<FlagFragment> flagFragment;
    @Inject
    Lazy<SearchFragment> searchFragment;
    @Inject
    Lazy<OcrFragment> ocrFragment;
    LoaderViewModel vm;
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
        return R.id.item_home;
    }

    @Override
    public boolean isHomeUp() {
        return false;
    }

    @Override
    public boolean hasRatePermitted() {
        return false;
    }

    @Override
    protected void onStartUi(Bundle state) {
        initView();
    }

    @NotNull
    @Override
    public BaseActivity getUiActivity() {
        return this;
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
            case R.id.item_favourite:
                commitFragment(FlagFragment.class, flagFragment, R.id.layout);
                break;
            case R.id.item_search:
                commitFragment(SearchFragment.class, searchFragment, R.id.layout);
                break;
            case R.id.item_ocr:
                commitFragment(OcrFragment.class, ocrFragment, R.id.layout);
                break;
            case R.id.item_more:
               // AndroidUtil.backupDatabase(getApplicationContext(), "frame-db");
                commitFragment(MoreFragment.class, moreFragment, R.id.layout);
                break;
        }
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public SearchView getSearchView() {
        return binding.searchView;
    }

    private void initView() {
        setTitle(null);
        binding = (ActivityNavigationBinding) super.binding;
        vm = ViewModelProviders.of(this, factory).get(LoaderViewModel.class);
        ad.loadBanner(findViewById(R.id.adview));
        //ad.loadInterstitial(R.string.debug_banner_ad_unit_id);
        vm.loads();
    }
}
