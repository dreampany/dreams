package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dreampany.framework.data.enums.UiState;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.injector.annote.FragmentScope;
import com.dreampany.framework.misc.exceptions.EmptyException;
import com.dreampany.framework.misc.exceptions.ExtraException;
import com.dreampany.framework.misc.exceptions.MultiException;
import com.dreampany.framework.ui.adapter.SmartAdapter;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ColorUtil;
import com.dreampany.framework.util.MenuTint;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.databinding.FragmentCoinsBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.adapter.CoinAdapter;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.FavoritesViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@FragmentScope
public class FavoritesFragment
        extends BaseMenuFragment
        implements SmartAdapter.Callback<CoinItem> {

    private static final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentCoinsBinding binding;
    private FavoritesViewModel vm;
    private CoinAdapter adapter;
    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;

    @Inject
    public FavoritesFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coins;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_favorites;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Companion.favoriteCoins(getAppContext());
    }


    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        vm.start();
    }


    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);
        vm.clear();
    }

    @Override
    public void onMenuCreated(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        MenuItem searchItem = findMenuItemById(R.id.item_search);
        MenuTint.colorMenuItem(searchItem, ColorUtil.Companion.getColor(getContext(), R.color.material_white), null);
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.refresh(!adapter.isEmpty(), true, true);
    }

    @Override
    public void onRefresh() {
        vm.refresh(!adapter.isEmpty(), true, true);
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText);
            adapter.filterItems();
        }
        return false;
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.button_favorite:
                vm.toggleFavorite((Coin) v.getTag());
                break;
            case R.id.button_alert:
                openCoinAlertUi((Coin) v.getTag());
                break;
        }
    }

    @Override
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            CoinItem item = adapter.getItem(position);
            openCoinUi(Objects.requireNonNull(item).getItem());
            return true;
        }
        return false;
    }

    @Override
    public boolean getEmpty() {
        return adapter == null || adapter.isEmpty();
    }

    @Nullable
    @Override
    public List<CoinItem> getItems() {
        return adapter.getCurrentItems();
    }


    @Nullable
    @Override
    public List<CoinItem> getVisibleItems() {
        return adapter.getVisibleItems();
    }

    @Nullable
    @Override
    public CoinItem getVisibleItem() {
        return adapter.getVisibleItem();
    }

    private void initView() {
        binding = (FragmentCoinsBinding) super.binding;
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.Companion.setText(this, R.id.text_empty, R.string.empty_favorites);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.Companion.setSwipe(refresh, this);
        UiTask<Coin> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(FavoritesViewModel.class);
        vm.setUiCallback(this);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputs(this, this::processResponse);
        vm.observeOutput(this, this::processSingleResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new CoinAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.Companion.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_coin, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                if (!refresh.isRefreshing()) {
                    refresh.setRefreshing(true);
                }
                break;
            case HIDE_PROGRESS:
                if (refresh.isRefreshing()) {
                    refresh.setRefreshing(false);
                }
                break;
            case OFFLINE:
                expandable.expand();
                break;
            case ONLINE:
                expandable.collapse();
                break;
            case EXTRA:
                processUiState(adapter.isEmpty() ? UiState.EMPTY : UiState.CONTENT);
                break;
            case EMPTY:
                binding.stateful.setState(EMPTY);
                break;
            case ERROR:
                break;
            case CONTENT:
                binding.stateful.setState(StatefulLayout.State.CONTENT);
                break;
        }
    }


    private void processResponse(Response<List<CoinItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress progress = (Response.Progress) response;
            processProgress(progress.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<CoinItem>> result = (Response.Result<List<CoinItem>>) response;
            processSuccess(result.getData());
        }
    }

    public void processSingleResponse(Response<CoinItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<CoinItem> result = (Response.Result<CoinItem>) response;
            processSingleSuccess(result.getData());
        }
    }

    private void processProgress(boolean loading) {
        if (loading) {
            vm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            vm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }

    private void processFailure(Throwable error) {
        if (error instanceof IOException || error.getCause() instanceof IOException) {
            vm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            vm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            vm.updateUiState(UiState.EXTRA);
        } else if (error instanceof MultiException) {
            for (Throwable e : ((MultiException) error).getErrors()) {
                processFailure(e);
            }
        }
    }

    private void processSuccess(List<CoinItem> items) {
        adapter.addFavoriteItems(items);
        ex.postToUi(() -> processUiState(UiState.EXTRA), 500);
    }

    private void processSingleSuccess(CoinItem item) {
        adapter.updateSilently(item);
    }

    private void openCoinUi(Coin coin) {
        UiTask<Coin> task = new UiTask<>(false, UiType.COIN, UiSubtype.VIEW, coin, null);
        openActivity(ToolsActivity.class, task);
    }

    private void openCoinAlertUi(Coin coin) {
        UiTask<Coin> task = new UiTask<>(false, UiType.COIN, UiSubtype.ALERT, coin, null);
        openActivity(ToolsActivity.class, task);
    }
}
