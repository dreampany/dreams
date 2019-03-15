package com.dreampany.lca.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.dreampany.frame.data.enums.Event;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.FragmentScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.databinding.FragmentIcoBinding;
import com.dreampany.lca.ui.activity.WebActivity;
import com.dreampany.lca.ui.adapter.IcoAdapter;
import com.dreampany.lca.ui.model.IcoItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.FinishedIcoViewModel;
import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import hugo.weaving.DebugLog;
import im.delight.android.webview.AdvancedWebView;
import net.cachapa.expandablelayout.ExpandableLayout;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@FragmentScope
public class FinishedIcoFragment
        extends BaseFragment
        implements SmartAdapter.Callback<IcoItem> {

    private static final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentIcoBinding binding;
    private FinishedIcoViewModel vm;
    private IcoAdapter adapter;
    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;

    @Inject
    public FinishedIcoFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ico;
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
    public void onResume() {
        super.onResume();
        vm.loads(!adapter.isEmpty(), adapter.isEmpty());
    }

    @Override
    public void onPause() {
        super.onPause();
        //vm.removeMultipleSubscription();
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
        }
    }*/

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
                vm.loads(true, adapter.isEmpty());
                break;
        }
    }

    @Override
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            IcoItem item = adapter.getItem(position);
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
    public List<IcoItem> getItems() {
        return adapter.getCurrentItems();
    }

    @Nullable
    @Override
    public List<IcoItem> getVisibleItems() {
        return adapter.getVisibleItems();
    }

    @Nullable
    @Override
    public IcoItem getVisibleItem() {
        return adapter.getVisibleItem();
    }

    private void initView() {
        binding = (FragmentIcoBinding) super.binding;

        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_finished_ico);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.setSwipe(refresh, this);

        UiTask<Ico> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(FinishedIcoViewModel.class);
        vm.setUiCallback(this);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        vm.observeEvent(this, this::processEvent);
        vm.observeOutputs(this, this::processResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new IcoAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_ico, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                refresh.setRefreshing(true);
                break;
            case HIDE_PROGRESS:
                refresh.setRefreshing(false);
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

    private void processEvent(Event event) {
        switch (event) {
            case NEW:
                break;
        }
    }

    @DebugLog
    private void processResponse(Response<List<IcoItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<IcoItem>> result = (Response.Result<List<IcoItem>>) response;
            processSuccess(result.getData());
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

    private void processSuccess(List<IcoItem> items) {
        if (scroller.isScrolling()) {
            return;
        }
        recycler.setNestedScrollingEnabled(false);
        adapter.addItems(items);
        recycler.setNestedScrollingEnabled(true);
        processUiState(UiState.EXTRA);
    }

    private void openCoinUi(Ico ico) {
        if (AdvancedWebView.Browsers.hasAlternative(getContext())) {
            AdvancedWebView.Browsers.openUrl(getParent(), ico.getIcoWatchListUrl());
        } else {
            UiTask<?> task = new UiTask<>(true);
            task.setComment(ico.getIcoWatchListUrl());
            openActivity(WebActivity.class, task);
        }
    }
}
