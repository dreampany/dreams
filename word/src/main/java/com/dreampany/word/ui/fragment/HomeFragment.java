package com.dreampany.word.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.activity.BaseActivity;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.callback.SearchViewCallback;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.databinding.FragmentItemsBinding;
import com.dreampany.word.ui.activity.ToolsActivity;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.dreampany.word.ui.enums.UiSubtype;
import com.dreampany.word.ui.enums.UiType;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.SearchViewModel;
import com.lapism.searchview.Search;
import com.lapism.searchview.database.SearchHistoryTable;
import com.lapism.searchview.widget.SearchAdapter;
import com.lapism.searchview.widget.SearchItem;
import com.lapism.searchview.widget.SearchView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseMenuFragment
        implements SmartAdapter.Callback<WordItem>,
        SearchAdapter.OnSearchItemClickListener,
        Search.OnQueryTextListener {

    private final String SEARCH = "search";
    private final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentItemsBinding binding;

    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;
    private SearchView searchView;
    private SearchAdapter searchAdapter;
    private SearchHistoryTable table;

    SearchViewModel searchVm;
    WordAdapter adapter;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_items;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_home;
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
        processUiState(UiState.HIDE_PROGRESS);
        if (searchView.isOpen()) {
            searchView.close();
        }
    }

    @Override
    public void onMenuCreated(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onMenuCreated(menu, inflater);
        BaseActivity activity = getParent();

        if (activity instanceof SearchViewCallback) {
            SearchViewCallback searchCallback = (SearchViewCallback) activity;
            searchView = searchCallback.getSearchView();
            initSearchView();
        }
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
                searchView.open(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean hasBackPressed() {
        if (searchView.isOpen()) {
            searchView.close();
            return true;
        }
        return super.hasBackPressed();
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

    @Override
    public void onSearchItemClick(int position, CharSequence title, CharSequence subtitle) {
        searchView.setQuery(title, true);
        //searchView.close();
    }

    @Override
    public boolean onQueryTextSubmit(CharSequence query) {
        SearchItem item = new SearchItem(getContext());
        item.setTitle(query);
        table.addItem(item);

        searchVm.search(query.toString(), false);
        return true;
    }

    @Override
    public void onQueryTextChange(CharSequence newText) {

    }

    @Override
    public List<WordItem> getVisibleItems() {
        return adapter.getVisibleItems();
    }

    @Override
    public WordItem getVisibleItem() {
        return adapter.getVisibleItem();
    }

    @Override
    public List<WordItem> getItems() {
        return null;
    }

    @Override
    public boolean getEmpty() {
        return false;
    }

    private void initView() {
        setTitle(R.string.home);
        binding = (FragmentItemsBinding) super.binding;
        binding.stateful.setStateView(SEARCH, LayoutInflater.from(getContext()).inflate(R.layout.item_search, null));
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        processUiState(UiState.SEARCH);
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_search_words);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.setSwipe(refresh, this);

        searchVm = ViewModelProviders.of(this, factory).get(SearchViewModel.class);
        searchVm.setUiCallback(this);
        searchVm.observeUiState(this, this::processUiState);
        searchVm.observeOutputs(this, this::processResponse);
        searchVm.observeOutput(this, this::processSingleResponse);

        searchAdapter = new SearchAdapter(getContext());
        table = new SearchHistoryTable(getContext());
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new WordAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener() {
            @Override
            public void onScrollingAtEnd() {

            }
        };
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(getContext()),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_word, searchVm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void initSearchView() {
        searchAdapter.setOnSearchItemClickListener(this);
        searchView.setAdapter(searchAdapter);
        searchView.setOnQueryTextListener(this);
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
            case SEARCH:
                binding.stateful.setState(SEARCH);
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

    @DebugLog
    public void processSingleResponse(Response<WordItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            //processSingleProgress(result.getLoading());
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
            searchVm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            searchVm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }

    private void processFailure(Throwable error) {
        if (error instanceof IOException || error.getCause() instanceof IOException) {
            searchVm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            searchVm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            searchVm.updateUiState(UiState.EXTRA);
        } else if (error instanceof MultiException) {
            for (Throwable e : ((MultiException) error).getErrors()) {
                processFailure(e);
            }
        }
    }

    private void processSuccess(List<WordItem> items) {
        Timber.v("Recent result %s", items.size());
        adapter.clear();
        adapter.addItems(items);
        ex.postToUi(() -> processUiState(UiState.EXTRA), 1000);
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
}
