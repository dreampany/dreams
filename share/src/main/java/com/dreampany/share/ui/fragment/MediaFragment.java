package com.dreampany.share.ui.fragment;

import android.app.Activity;
import android.app.SearchManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.databinding.FragmentTabpagerFixedBinding;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.fragment.BaseStateFragment;
import com.dreampany.framework.util.TextUtil;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.media.data.model.Media;
import com.dreampany.share.R;
import com.dreampany.share.data.model.SelectEvent;
import com.dreampany.share.ui.model.UiTask;
import com.dreampany.share.vm.MediaViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;



/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class MediaFragment extends BaseStateFragment<BaseFragment> {

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentTabpagerFixedBinding binding;
    private MediaViewModel vm;
    MenuItem editItem;

    @Inject
    public MediaFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpager_fixed;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_share;
    }

    @NotNull
    @Override
    protected String[] pageTitles() {
        return TextUtil.getStrings(getContext(), R.string.apk, R.string.image);
    }

    @NotNull
    @Override
    protected Class<BaseFragment>[] pageClasses() {
        return new Class[]{ApkFragment.class, ImageFragment.class};
    }

    @NotNull
    @Override
    protected Task<?>[] pageTasks() {
        UiTask<?> task = getCurrentTask(false);
        return new Task<?>[]{task, task};
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
    public void onMenuCreated(@NotNull Menu menu) {
        initMenu(menu);
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
    }

    @Override
    protected void onStopUi() {

    }

    @NotNull
    @Override
    public BaseFragment getUiFragment() {
        return this;
    }

    @NonNull
    @Override
    public ViewModel getX() {
        return vm;
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit:
                processDone(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        setTitle(ViewUtil.getText(getContext(), R.string.selected, 0));
        setSubtitle(ViewUtil.getText(getContext(), R.string.total, 0));
        binding = (FragmentTabpagerFixedBinding) super.binding;
        UiTask<Media> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(MediaViewModel.class);
        vm.setTask(uiTask);
        vm.observeSelect(this, this::onSelect);
    }

    private void initMenu(Menu menu) {
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

        editItem = menu.findItem(R.id.item_edit);

        searchItem.setVisible(true);
        editItem.setIcon(R.drawable.ic_done_white_24dp);
        editItem.setVisible(false);
    }


    private void onSelect(Set<SelectEvent> events) {
        int selected = 0;
        int total = 0;
        for (SelectEvent event : events) {
            selected += event.getSelected();
            total += event.getTotal();
        }
        setTitle(ViewUtil.getText(getContext(), R.string.selected, selected));
        setSubtitle(ViewUtil.getText(getContext(), R.string.total, total));

        editItem.setVisible(selected > 0);
    }

    private void processDone(MenuItem item) {
        List<BaseFragment> fragments = getFragments();
        if (fragments != null) {
            for (BaseFragment fragment : fragments) {
                fragment.onOptionsItemSelected(item);
            }
        }
    }
}
