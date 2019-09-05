package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dreampany.framework.data.enums.UiState;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.misc.exception.EmptyException;
import com.dreampany.framework.misc.exception.ExtraException;
import com.dreampany.framework.misc.exception.MultiException;
import com.dreampany.framework.ui.adapter.SmartAdapter;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ColorUtil;
import com.dreampany.framework.util.MenuTint;
import com.dreampany.framework.util.TextUtil;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.databinding.FragmentCoinsBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.adapter.CoinAdapter;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.CoinsViewModel;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.fastscroller.FastScroller;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

import net.cachapa.expandablelayout.ExpandableLayout;


import timber.log.Timber;

import javax.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@ActivityScope
public class CoinsFragment
        extends BaseMenuFragment
        implements SmartAdapter.Callback<CoinItem> {

    private static final String NONE = "none";
    private static final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentCoinsBinding binding;

    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;

    private CoinsViewModel vm;
    private CoinAdapter adapter;

    @Inject
    public CoinsFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coins;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_coins;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @NonNull
    @Override
    public String getScreen() {
        return Constants.Companion.coins(getAppContext());
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
    public void onMenuCreated(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem refreshItem = findMenuItemById(R.id.item_refresh);
        MenuItem searchItem = findMenuItemById(R.id.item_search);
        MenuTint.colorMenuItem(refreshItem, ColorUtil.getColor(getContext(), R.color.material_white), null);
        MenuTint.colorMenuItem(searchItem, ColorUtil.getColor(getContext(), R.color.material_white), null);
        initCurrencyMenuItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        initCurrencyMenuItem();
        vm.refresh(!adapter.isEmpty(), true, true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_currency:
                openCurrencyPicker();
                return true;
            case R.id.item_refresh:
                vm.refresh(!adapter.isEmpty(), true, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        vm.refresh(!adapter.isEmpty(), true, true);
    }

    @Override
    public void onLoadMore(int lastPosition, int currentPage) {
        Timber.v("LastPosition %d CurrentPage %d", lastPosition, currentPage);
        if (adapter.hasFilter()) {
            adapter.onLoadMoreComplete(null);
            return;
        }
        //vm.loads(lastPosition, true, false);
    }

    @Override
    public void noMoreLoad(int newItemsSize) {
        //super.noMoreLoad(newItemsSize);
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
        setTitle(R.string.coins);
        setSubtitle(TextUtil.getString(getContext(), R.string.coins_total, 0));
        binding = (FragmentCoinsBinding) super.binding;
        binding.stateful.setStateView(NONE, LayoutInflater.from(getContext()).inflate(R.layout.item_none, null));
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        binding.stateful.setState(NONE);

        ViewUtil.setText(this, R.id.text_loading, R.string.loading_coins);
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_coins);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.setSwipe(refresh, this);
        UiTask<Coin> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(CoinsViewModel.class);
        vm.setUiCallback(this);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputs(this, this::processResponse);
        vm.observeOutput(this, this::processSingleResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        FastScroller fs = ViewUtil.getViewById(this, R.id.fast_scroller);
        adapter = new CoinAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener(true) {

            @Override
            public void onScrollingAtEnd() {
                Timber.v("onScrollingAtEnd");
                vm.refresh(true, false, true);
            }

            @Override
            public void onScrolledToBottom() {
                Timber.v("onScrolledToBottom");
                vm.loads(adapter.getItemCount(), !adapter.isEmpty(), true);
            }
        };
        //adapter.setEndlessScrollListener(this, CoinItem.getProgressItem());
        //adapter.setEndlessScrollThreshold(Constants.Limit.COIN_THRESHOLD);
        //adapter.setEndlessPageSize(Constants.Limit.COIN_PAGE);
        //adapter.setEndlessTargetCount(Constants.Limit.COIN_PAGE_MAX);
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_coin, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller);
        // adapter.setFastScroller(fs);
    }

    private void initCurrencyMenuItem() {
        String currency = vm.getCurrentCurrencyCode();
        MenuItem currencyItem = findMenuItemById(R.id.item_currency);
        if (currencyItem != null) {
            currencyItem.setTitle(currency);
        }
    }

    private void processUiState(UiState state) {
        switch (state) {
            case NONE:
                binding.stateful.setState(NONE);
                break;
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
                if (isVisible()) {
                    setSubtitle(TextUtil.getString(getContext(), R.string.coins_total, adapter.getItemCount()));
                }
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


    public void processResponse(Response<List<CoinItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<CoinItem>> result = (Response.Result<List<CoinItem>>) response;
            processResult(result.getData());
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

    private void processResult(List<CoinItem> items) {
        Timber.v("Coins %s", items.size());
        adapter.addItems(items);
        ex.postToUi(() -> processUiState(UiState.EXTRA));
    }

    private void processSingleSuccess(CoinItem item) {
        adapter.updateSilently(item);
    }

    private void updateCurrency(Currency currency) {
        adapter.updateCurrency(currency);
    }

    private void openCoinUi(Coin coin) {
        UiTask<Coin> task = new UiTask<>(false);
        task.setInput(coin);
        task.setUiType(UiType.COIN);
        task.setSubtype(UiSubtype.VIEW);
        openActivity(ToolsActivity.class, task);
    }

    private void openFavoritesUi() {
        UiTask<Coin> task = new UiTask<>(false);
        task.setUiType(UiType.COIN);
        task.setSubtype(UiSubtype.FAVORITES);
        openActivity(ToolsActivity.class, task);
    }

    private void openCoinAlertUi(Coin coin) {
        UiTask<Coin> task = new UiTask<>(false);
        task.setInput(coin);
        task.setUiType(UiType.COIN);
        task.setSubtype(UiSubtype.ALERT);
        openActivity(ToolsActivity.class, task);
    }

    private void openCurrencyPicker() {
        List<ExtendedCurrency> currencies = vm.getCurrencies();

        CurrencyPicker picker = CurrencyPicker.newInstance(getString(R.string.select_currency));
        picker.setCurrenciesList(currencies);
        picker.setListener((name, code, symbol, flagDrawableResId) -> {
            vm.setCurrentCurrencyCode(code);
            initCurrencyMenuItem();
            updateCurrency(vm.getCurrentCurrency());
            onRefresh();
            picker.dismissAllowingStateLoss();
        });
        picker.show(getFragmentManager(), Constants.Tag.CURRENCY_PICKER);
    }
}
