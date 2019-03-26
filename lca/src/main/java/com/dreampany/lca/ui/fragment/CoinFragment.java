package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.dreampany.frame.data.model.Task;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.fragment.BaseStateFragment;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@ActivityScope
public class CoinFragment extends BaseStateFragment<BaseFragment> implements SearchView.OnQueryTextListener {

    @Inject
    public CoinFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tabpager_fixed;
    }

    @NonNull
    @Override
    protected String[] pageTitles() {
        return TextUtil.getStrings(getContext(), R.string.details, R.string.market, R.string.graph);
    }

    @NonNull
    @Override
    protected Class<BaseFragment>[] pageClasses() {
        return new Class[]{CoinDetailsFragment.class, MarketFragment.class, GraphFragment.class};
    }

    @NotNull
    @Override
    protected Task<?>[] pageTasks() {
        UiTask<Coin> task = getCurrentTask(false);
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
        return Constants.Screen.coin(getAppContext());
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
    }

    private void initView() {
        UiTask<Coin> task = getCurrentTask(false);
        setTitle(Objects.requireNonNull(task).getInput().getSymbol());
    }


}
