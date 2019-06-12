package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dreampany.frame.data.model.Task;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.fragment.BaseStateFragment;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.frame.util.MenuTint;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class IcoFragment extends BaseStateFragment<BaseFragment> {

    @Inject
    public IcoFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpager_fixed;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_ico;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @NonNull
    @Override
    protected String[] pageTitles() {
        return TextUtil.getStrings(getContext(), R.string.live, R.string.upcoming, R.string.finished);
    }

    @NonNull
    @Override
    protected Class<BaseFragment>[] pageClasses() {
        return new Class[]{LiveIcoFragment.class, UpcomingIcoFragment.class, FinishedIcoFragment.class};
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

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Screen.ico(getAppContext());
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public void onMenuCreated(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        MenuItem searchItem = findMenuItemById(R.id.item_search);
        MenuTint.colorMenuItem(searchItem, ColorUtil.getColor(getContext(), R.color.material_white), null);
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
    }

    private void initView() {
        setTitle(R.string.ico);
        setSubtitle(null);
    }
}
