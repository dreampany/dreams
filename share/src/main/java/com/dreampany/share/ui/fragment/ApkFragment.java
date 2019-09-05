package com.dreampany.share.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.dreampany.framework.api.notify.NotifyManager;
import com.dreampany.framework.data.enums.UiState;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.FragmentScope;
import com.dreampany.framework.misc.exception.EmptyException;
import com.dreampany.framework.misc.exception.ExtraException;
import com.dreampany.framework.misc.exception.MultiException;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.databinding.FragmentMediaBinding;
import com.dreampany.share.R;
import com.dreampany.share.misc.exception.SelectException;
import com.dreampany.share.ui.adapter.MediaAdapter;
import com.dreampany.share.ui.model.MediaItem;
import com.dreampany.share.ui.model.UiTask;
import com.dreampany.share.vm.ApkViewModel;
import com.dreampany.share.vm.MediaViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;


/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@FragmentScope
public class ApkFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentMediaBinding binding;
    private MediaViewModel mvm;
    private ApkViewModel vm;
    private MediaAdapter adapter;
    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;

    @Inject
    public ApkFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_media;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
    }

    @Override
    protected void onStopUi() {
        vm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.loadsWithShare(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        vm.removeMultipleSubscription();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isResumed()) {
            return;
        }
        if (isVisibleToUser) {
            vm.loadsWithShare(false);
        } else {
            vm.removeMultipleSubscription();
        }
    }

    @Override
    public void onRefresh() {
        vm.loadsWithShare(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit:
                vm.selectToShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            MediaItem item = adapter.getItem(position);
            vm.toggleSelect((Apk) Objects.requireNonNull(item).getItem());
            return true;
        }
        return false;
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.button_like:
                vm.toggleSelect((Apk) v.getTag());
                break;
        }
    }

    private void initView() {
        binding = (FragmentMediaBinding) super.binding;
        refresh = binding.layoutRefresh;
        expandable = findViewById(R.id.layout_expandable);
        recycler = findViewById(R.id.recycler);

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.button_empty);

        UiTask<Apk> uiTask = getCurrentTask(true);
        mvm = (MediaViewModel) getFragmentCallback().getX();
        vm = ViewModelProviders.of(this, factory).get(ApkViewModel.class);
        vm.setTask(uiTask);
        vm.observeSelect(this, mvm::onSelect);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputs(this, this::processResponse);
        vm.observeOutput(this, this::processSingleResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new MediaAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollGridLayoutManager(Objects.requireNonNull(getContext()), 4),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_apk, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }


    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                if (adapter.isEmpty()) {
                    refresh.setRefreshing(true);
                }
                break;
            case HIDE_PROGRESS:
                refresh.setRefreshing(false);
                break;
            case EXTRA:
                processUiState(adapter.isEmpty() ? UiState.EMPTY : UiState.CONTENT);
                break;
            case EMPTY:
            case ERROR:
                binding.stateful.showEmpty();
                break;
            case CONTENT:
                binding.stateful.showContent();
                break;
        }
    }

    public void processResponse(Response<List<MediaItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<MediaItem>> result = (Response.Result<List<MediaItem>>) response;
            processSuccess(result.getData());
        }
    }

    public void processSingleResponse(Response<MediaItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<MediaItem> result = (Response.Result<MediaItem>) response;
            processSingleSuccess(result.getData());
        }
    }

    public void onSelect(MediaItem item) {
        adapter.updateSilently(item);
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
        } else if (error instanceof SelectException) {
            showInfo(error.getMessage());
        }
    }

    private void processSuccess(List<MediaItem> items) {
        if (scroller.isScrolling()) {
            return;
        }
        recycler.setNestedScrollingEnabled(false);
        adapter.addItems(items);
        recycler.setNestedScrollingEnabled(true);
        processUiState(UiState.EXTRA);
    }

    private void processSingleSuccess(MediaItem item) {
        adapter.updateSilently(item);
    }
}
