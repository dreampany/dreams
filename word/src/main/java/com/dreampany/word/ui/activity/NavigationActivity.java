package com.dreampany.word.ui.activity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.misc.SmartAd;
import com.dreampany.framework.ui.activity.BaseBottomNavigationActivity;
import com.dreampany.framework.ui.callback.SearchViewCallback;
import com.dreampany.word.R;
import com.dreampany.word.databinding.ActivityNavigationBinding;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.fragment.FavoriteWordsFragment;
import com.dreampany.word.ui.fragment.HomeFragment;
import com.dreampany.word.ui.fragment.MoreFragment;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.vm.LoaderViewModel;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NavigationActivity extends BaseBottomNavigationActivity implements SearchViewCallback {

    @Inject
    Lazy<MoreFragment> moreFragment;
    @Inject
    Lazy<HomeFragment> homeFragment;
    @Inject
    Lazy<FavoriteWordsFragment> favoritesFragment;
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    SmartAd ad;

    private ActivityNavigationBinding bind;
    private LoaderViewModel vm;

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
    protected void onStopUi() {
        ad.destroyBanner(getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.resumeBanner(getClass().getSimpleName());
        //vm.loads();
    }

    @Override
    protected void onPause() {
        ad.pauseBanner(getClass().getSimpleName());
        super.onPause();
    }

    @Override
    protected void onNavigationItem(int navigationItemId) {
        switch (navigationItemId) {
            case R.id.item_home:
                commitFragment(HomeFragment.class, homeFragment, R.id.layout);
                break;
            case R.id.item_favorites:
                commitFragment(FavoriteWordsFragment.class, favoritesFragment, R.id.layout);
                break;
            case R.id.item_more:
                commitFragment(MoreFragment.class, moreFragment, R.id.layout);
                break;
        }
    }

    @Override
    public MaterialSearchView getSearchView() {
        return bind.searchView;
    }

    @Override
    public void execute(Task<?> t) {

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

        vm = ViewModelProviders.of(this, factory).get(LoaderViewModel.class);
    }
}
