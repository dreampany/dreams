package com.dreampany.music.ui.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.dreampany.frame.data.model.Task;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.fragment.BaseStateFragment;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.music.R;
import com.dreampany.music.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

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
        return TextUtil.getStrings(getContext(), R.string.songs, R.string.albums, R.string.artists);
    }

    @NonNull
    @Override
    protected Class<BaseFragment>[] pageClasses() {
        return new Class[]{SongsFragment.class, AlbumsFragment.class, ArtistsFragment.class};
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
        setTitle(R.string.home);
    }

    @Override
    protected void onStopUi() {

    }

    @DebugLog
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

    }
}
