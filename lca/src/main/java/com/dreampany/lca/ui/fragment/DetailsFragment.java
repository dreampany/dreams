package com.dreampany.lca.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.FragmentScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.databinding.FragmentDetailsBinding;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.adapter.CoinAdapter;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.CoinViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import hugo.weaving.DebugLog;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@FragmentScope
public class DetailsFragment
        extends BaseFragment
        implements SmartAdapter.Callback<CoinItem> {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentDetailsBinding binding;
    CoinViewModel vm;
    CoinAdapter adapter;
    OnVerticalScrollListener scroller;
    SwipeRefreshLayout refresh;
    ExpandableLayout expandable;
    RecyclerView recycler;

    @Inject
    public DetailsFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_details;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        vm.start();
    }

    @Override
    protected void onStopUi() {
        vm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.refresh(!adapter.isEmpty(), adapter.isEmpty());
    }

    @Override
    public void onPause() {
        //vm.removeMultipleSubscription();
        vm.removeUpdateDisposable();
        super.onPause();
    }

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
            vm.removeUpdateDisposable();
        }
    }*/

    @Override
    public void onRefresh() {
        vm.refresh(!adapter.isEmpty(), true);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.button_like:
                vm.toggleFavorite((Coin) v.getTag());
                break;
            case R.id.fab:
                openCoinAlertUi();
                break;
            case R.id.button_empty:
                //vm.loads(true);
                break;
        }
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
        binding = (FragmentDetailsBinding) super.binding;
        refresh = binding.layoutRefresh;
        expandable = binding.layoutTop.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        binding.fab.setOnClickListener(this);

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.button_empty);

        UiTask<Coin> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(CoinViewModel.class);
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
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_coin_details, vm.getItemOffset())
                        .addItemViewType(R.layout.item_coin_quote, vm.getItemOffset())
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
        }
    }

    @DebugLog
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
        if (error instanceof IOException) {
            vm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            vm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            vm.updateUiState(UiState.EXTRA);
        }
    }

    private void processSuccess(List<CoinItem> items) {
        if (scroller.isScrolling()) {
            return;
        }
        //recycler.setNestedScrollingEnabled(false);
        adapter.addItems(items);
        //recycler.setNestedScrollingEnabled(true);
        processUiState(UiState.EXTRA);
    }

    private void processSingleSuccess(CoinItem item) {
        adapter.updateSilently(item);
    }

    private void openCoinAlertUi() {
        UiTask<Coin> task = getCurrentTask();
        task.setUiType(UiType.COIN);
        task.setSubtype(UiSubtype.ALERT);
        openActivity(ToolsActivity.class, task);
    }
}
