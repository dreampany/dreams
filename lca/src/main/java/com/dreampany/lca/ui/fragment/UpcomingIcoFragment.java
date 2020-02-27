package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.dreampany.framework.misc.exception.EmptyException;
import com.dreampany.framework.misc.exception.ExtraException;
import com.dreampany.framework.misc.exception.MultiException;
import com.dreampany.framework.ui.adapter.SmartAdapter;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.databinding.FragmentIcoBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.adapter.IcoAdapter;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.IcoItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.UpcomingIcoViewModel;
import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import net.cachapa.expandablelayout.ExpandableLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

//import com.dreampany.frame.data.enums.EventType;


/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@FragmentScope
public class UpcomingIcoFragment
        extends BaseFragment
        implements SmartAdapter.Callback<IcoItem> {

    private static final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentIcoBinding binding;
    private UpcomingIcoViewModel vm;
    private IcoAdapter adapter;
    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;

    @Inject
    public UpcomingIcoFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ico;
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Companion.icoUpcoming(getAppContext());
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        vm.start();
        vm.loads(!adapter.isEmpty(), adapter.isEmpty());
    }

    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);
        //vm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       // vm.removeMultipleSubscription();
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
        ViewUtil.Companion.setText(this, R.id.text_empty, R.string.empty_upcoming_ico);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.Companion.setSwipe(refresh, this);

        UiTask<Ico> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(UpcomingIcoViewModel.class);
        vm.setUiCallback(this);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        //vm.observeEvent(this, this::processEvent);
        vm.observeOutputs(this, this::processResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new IcoAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.Companion.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_ico, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                findViewById(R.id.empty_view)
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

/*    private void processEvent(EventType eventType) {
        switch (eventType) {
            case NEW:
                break;
        }
    }*/


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
        adapter.addItems(items);
        ex.postToUi(() -> processUiState(UiState.EXTRA), 1000);
    }

    private void openCoinUi(Ico ico) {
        UiTask<Ico> task = new UiTask<>(true, UiType.ICO, UiSubtype.VIEW, null, ico.getIcoWatchListUrl());
        openActivity(ToolsActivity.class, task);
    }
}
