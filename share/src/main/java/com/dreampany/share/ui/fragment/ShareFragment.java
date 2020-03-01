package com.dreampany.share.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.dreampany.framework.data.enums.UiState;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.misc.exceptions.EmptyException;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.media.data.model.Media;
import com.dreampany.share.R;
import com.dreampany.share.databinding.FragmentShareBinding;
import com.dreampany.share.ui.activity.ToolsActivity;
import com.dreampany.share.ui.adapter.MediaAdapter;
import com.dreampany.share.ui.enums.UiSubtype;
import com.dreampany.share.ui.enums.UiType;
import com.dreampany.share.ui.model.MediaItem;
import com.dreampany.share.ui.model.UiTask;
import com.dreampany.share.vm.ShareViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class ShareFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentShareBinding binding;
    ShareViewModel vm;
    MediaAdapter adapter;
    OnVerticalScrollListener scroller;
    SwipeRefreshLayout refresh;
    ExpandableLayout expandable;
    RecyclerView recycler;

    @Inject
    public ShareFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share;
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
        vm.loads(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        vm.removeMultipleSubscription();
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.fab:
                openMediaUi();
                break;
        }
    }

    private void initView() {
        setTitle(R.string.share);
        binding = (FragmentShareBinding) super.binding;
        refresh = binding.layoutRefresh;
        expandable = findViewById(R.id.layout_expandable);
        recycler = findViewById(R.id.recycler);

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.button_empty);
        ViewUtil.setClickListener(this, R.id.fab);

        vm = ViewModelProviders.of(this, factory).get(ShareViewModel.class);
        UiTask<Media> uiTask = getCurrentTask(true);
        vm.setTask(uiTask);
        vm.observeUiState(this, this::processUiState);
        //     vm.observeEvent(this, this::processEvent);
        //     vm.observeOutput(this, this::processSingleResponse);
        vm.observeOutputs(this, this::processResponse);

    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new MediaAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
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
            Response.Progress progress = (Response.Progress) response;
            processProgress(progress.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<MediaItem>> result = (Response.Result<List<MediaItem>>) response;
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
        if (error instanceof IOException) {
            vm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            vm.updateUiState(UiState.EMPTY);
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

    private void openMediaUi() {
        UiTask<Media> task = new UiTask<>(false);
        task.setUiType(UiType.MEDIA);
        task.setSubtype(UiSubtype.EDIT);
        openActivity(ToolsActivity.class, task);
    }

}
