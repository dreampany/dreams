package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.databinding.FragmentCoinsBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.adapter.CoinAlertAdapter;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.CoinAlertViewModel;
import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import hugo.weaving.DebugLog;
import net.cachapa.expandablelayout.ExpandableLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@ActivityScope
public class CoinAlertsFragment
        extends BaseMenuFragment {

    private static final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentCoinsBinding binding;
    private CoinAlertViewModel vm;
    private CoinAlertAdapter adapter;
    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;

    @Inject
    public CoinAlertsFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coins;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_alerts;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @NotNull
    @Override
    public String getAnalyticTag() {
        return Constants.Tag.UI_COIN_ALERTS;
    }

    @DebugLog
    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
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
        vm.loads(false, adapter.isEmpty());
    }

/*    @Override
    public void onPause() {
        vm.removeMultipleSubscription();
        vm.removeUpdateDisposable();
        vm.clearInputs();
        super.onPause();
    }*/
 /*
    @Override
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
            vm.removeUpdateItemDisposable();
            vm.removeUpdateVisibleItemsDisposable();
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                adapter.toggleDelete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        vm.loads(!adapter.isEmpty(), true);
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
            case R.id.button_empty:
                //vm.loads(true, true);
                break;
            case R.id.image_delete:
                CoinAlertItem item = (CoinAlertItem) v.getTag();
                Timber.v("Delete fired");
                vm.delete(item, true);
                //adapter.removeItem(item);
                break;
        }
    }

    @Override
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            CoinAlertItem item = adapter.getItem(position);
            //openCoinUi(Objects.requireNonNull(item).getItem());
            return true;
        }
        return false;
    }

    private void initView() {
        setTitle(R.string.alerts);
        setSubtitle(null);
        binding = (FragmentCoinsBinding) super.binding;
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_alerts);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.setSwipe(refresh, this);
        UiTask<CoinAlert> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(CoinAlertViewModel.class);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutput(this, this::processResponse);
        vm.observeOutputs(this, this::processResponses);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new CoinAlertAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_coin_alert, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null,
                null,
                null
        );
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                if (adapter.isEmpty()) {
                    if (!refresh.isRefreshing()) {
                        refresh.setRefreshing(true);
                    }
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

    private void processResponse(Response<CoinAlertItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress progress = (Response.Progress) response;
            processProgress(progress.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<CoinAlertItem> result = (Response.Result<CoinAlertItem>) response;
            processResult(result.getType(), result.getData());
        }
    }

    private void processResponses(Response<List<CoinAlertItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress progress = (Response.Progress) response;
            processProgress(progress.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<CoinAlertItem>> result = (Response.Result<List<CoinAlertItem>>) response;
            processResult(result.getType(), result.getData());
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
        if (error instanceof EmptyException) {
            vm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            vm.updateUiState(UiState.EXTRA);
        }
    }

    private void processResult(Response.Type type, CoinAlertItem result) {
        if (scroller.isScrolling()) {
            return;
        }
        if (type == Response.Type.DELETE) {
            adapter.removeItem(result);
        } else {
            adapter.addItem(result);
        }
        AndroidUtil.getUiHandler().postDelayed(() -> processUiState(UiState.EXTRA), 1000);
    }

    private void processResult(Response.Type type, List<CoinAlertItem> items) {
        if (scroller.isScrolling()) {
            return;
        }
        if (type == Response.Type.DELETE) {
            adapter.removeItem(items);
        } else {
            adapter.addItems(items);
        }
        AndroidUtil.getUiHandler().postDelayed(() -> processUiState(UiState.EXTRA), 1000);
    }

    private void openCoinAlertUi() {
        UiTask<Coin> task = getCurrentTask();
        task.setUiType(UiType.COIN);
        task.setSubtype(UiSubtype.ALERT);
        openActivity(ToolsActivity.class, task);
    }
}