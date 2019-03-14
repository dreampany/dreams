package com.dreampany.lca.ui.fragment;

import android.view.MenuItem;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.databinding.FragmentCoinsBinding;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.adapter.CoinAdapter;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.CoinsViewModel;
import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.fastscroller.FastScroller;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import hugo.weaving.DebugLog;
import net.cachapa.expandablelayout.ExpandableLayout;
import org.jetbrains.annotations.Nullable;
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

    private static final String LOADING = "loading";
    private static final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentCoinsBinding binding;

    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;
    private View empty, filter;

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

    @DebugLog
    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        vm.start();
    }

    @DebugLog
    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);
        vm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.refresh(!adapter.isEmpty(), adapter.isEmpty());
    }

/*    @Override
    public void onPause() {
        vm.removeMultipleSubscription();
        vm.removeSingleSubscription();
        vm.removeUpdateDisposable();
        vm.removeUpdateItemDisposable();
        vm.removeUpdateVisibleItemsDisposable();
        super.onPause();
    }*/

/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isResumed()) {
            return;
        }
        if (isVisibleToUser) {
            vm.loads(false);
        } else {
            vm.removeMultipleSubscription();
            vm.removeSingleSubscription();
            vm.removeUpdateDisposable();
            vm.removeUpdateItemDisposable();
            vm.removeUpdateVisibleItemsDisposable();
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_favorites:
                openFavoritesUi();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        vm.refresh(!adapter.isEmpty(), true);
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

/*    @Override
    public void onUpdateEmptyDataView(int size) {
        if (size > 0) {
            processUiState(UiState.CONTENT);
        } else  {
            processUiState(UiState.EMPTY);
        }
    }

    @Override
    public void onUpdateEmptyFilterView(int size) {
        if (size > 0) {
            processUiState(UiState.CONTENT);
        } else  {
            processUiState(UiState.EMPTY);
        }
    }*/

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
            case R.id.button_empty:
                vm.loads(true, true);
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
        binding.stateful.setStateView(LOADING, LayoutInflater.from(getContext()).inflate(R.layout.item_loading, null));
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.setText(this, R.id.text_loading, R.string.loading_coins);
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_coins);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;
        empty = binding.getRoot().findViewById(R.id.empty_view);
        filter = binding.getRoot().findViewById(R.id.filter_view);

        ViewUtil.setSwipe(refresh, this);

        UiTask<Coin> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(CoinsViewModel.class);
        vm.setUiCallback(this);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputs(this, this::processResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        FastScroller fs = ViewUtil.getViewById(this, R.id.fast_scroller);
        adapter = new CoinAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener(true) {
            @Override
            public void onScrolledToBottom() {
                vm.loadMore(!adapter.isEmpty(), true);
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

    @DebugLog
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
                setSubtitle(TextUtil.getString(getContext(), R.string.coins_total, adapter.getItemCount()));
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
        if (scroller.isScrolling()) {
            return;
        }
        //recycler.setNestedScrollingEnabled(false);
        Timber.v("Coins %s", items.size());
        adapter.addItems(items);
        //adapter.loadMoreComplete(items);
        //recycler.setNestedScrollingEnabled(true);
        AndroidUtil.getUiHandler().postDelayed(() -> processUiState(UiState.EXTRA), 500);
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
}
