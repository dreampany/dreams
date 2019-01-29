package com.dreampany.word.ui.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.dreampany.frame.data.enums.UiMode;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.activity.BaseActivity;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.callback.SearchViewCallback;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.databinding.FragmentRecentsBinding;
import com.dreampany.word.ui.activity.ToolsActivity;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.dreampany.word.ui.enums.UiSubtype;
import com.dreampany.word.ui.enums.UiType;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.RecentsViewModel;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import hugo.weaving.DebugLog;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@ActivityScope
public class RecentsFragment extends BaseMenuFragment implements SmartAdapter.Callback<WordItem> {

    private static final String EMPTY = "empty";
    private static final String SEARCH_EMPTY = "search_empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentRecentsBinding binding;
    private RecentsViewModel vm;
    private WordAdapter adapter;
    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;
    private MaterialSearchView searchView;

    @Inject
    public RecentsFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recents;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_recents;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
    }

    @Override
    protected void onStopUi() {
        //vm.clear();
/*        if (searchView != null) {
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            }
            searchView.setVisibility(View.GONE);
        }*/
    }

    @Override
    public boolean hasBackPressed() {
/*        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            return true;
        }*/
        return super.hasBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.loads(false);
    }

    @Override
    public void onPause() {
        vm.removeMultipleSubscription();
        vm.removeUpdateDisposable();
        super.onPause();
    }

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
            vm.removeUpdateDisposable();
        }
    }


    @Override
    public void onRefresh() {
        vm.loads(true);
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
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            WordItem item = adapter.getItem(position);
            openUi(item.getItem());
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public List<WordItem> getVisibleItems() {
        return adapter.getVisibleItems();
    }

    @Nullable
    @Override
    public WordItem getVisibleItem() {
        return adapter.getVisibleItem();
    }

    @DebugLog
    private void initView() {
        setTitle(R.string.recent_all);
        binding = (FragmentRecentsBinding) super.binding;
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        binding.stateful.setStateView(SEARCH_EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_search_empty, null));

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTop.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        BaseActivity activity = getParent();
        if (activity instanceof SearchViewCallback) {
            SearchViewCallback searchCallback = (SearchViewCallback) activity;
            //searchView = searchCallback.getSearchView();
        }

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.button_empty);
        UiTask<Word> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(RecentsViewModel.class);
        vm.setTask(uiTask);
        vm.setUiCallback(this);
        vm.observeUiMode(this, this::processUiMode);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputs(this, this::processResponse);
        vm.observeOutput(this, this::processSingleResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new WordAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener() {
            @Override
            public void onScrolling() {
                vm.update();
            }
        };
        //adapter.setEndlessScrollListener(this, CoinItem.getProgressItem());
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(getContext()),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_word, vm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void processUiMode(UiMode mode) {
        switch (mode) {
            case MAIN:
                vm.loads(false);
                break;
            case EDIT:
                break;
            case SEARCH:
                vm.updateUiState(UiState.SEARCH);
                break;
        }
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
/*            case EMPTY:
                if (adapter.isEmpty()) {
                    processUiState(UiState.EMPTY);
                } else {
                    processUiState(UiState.HIDE_PROGRESS);
                }
                break;*/
            case ERROR:
                break;
            case CONTENT:
                binding.stateful.setState(StatefulLayout.State.CONTENT);
                break;
            case SEARCH:
                //adapter.clear();
                //todo show search related interface
                //binding.stateful.setState(SEARCH_EMPTY);
                break;

        }
    }

    public void processResponse(Response<List<WordItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<WordItem>> result = (Response.Result<List<WordItem>>) response;
            processSuccess(result.getData());
        }
    }

    public void processSingleResponse(Response<WordItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<WordItem> result = (Response.Result<WordItem>) response;
            processSingleSuccess(result.getData());
        }
    }

    private void processProgress(boolean loading) {
        if (loading) {
            if (adapter.isEmpty()) {
                vm.updateUiState(UiState.SHOW_PROGRESS);
            }
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

    private void processSuccess(List<WordItem> items) {
        if (scroller.isScrolling()) {
            return;
        }
        recycler.setNestedScrollingEnabled(false);
        adapter.addItemsByRecent(items);
        recycler.setNestedScrollingEnabled(true);
        processUiState(UiState.EXTRA);
    }

    private void processSingleSuccess(WordItem item) {
        adapter.updateSilently(item);
    }

    private void openUi(Word item) {
        UiTask<Word> task = new UiTask<>(false);
        task.setInput(item);
        task.setUiType(UiType.WORD);
        task.setSubtype(UiSubtype.VIEW);
        openActivity(ToolsActivity.class, task);
    }

    private void openAll() {
        UiTask<Word> task = new UiTask<>();
        task.setUiType(UiType.WORD);
        task.setSubtype(UiSubtype.RECENTS);
        task.setFull(true);
        openActivity(ToolsActivity.class, task);
    }

    @Nullable
    @Override
    public List<WordItem> getItems() {
        return null;
    }
}
