package com.dreampany.word.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.databinding.FragmentHomeBinding;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.RecentViewModel;
import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;
import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import net.cachapa.expandablelayout.ExpandableLayout;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseMenuFragment implements SmartAdapter.Callback<WordItem> {

    private final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentHomeBinding binding;

    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private RecyclerView recycler;
    private SearchView searchView;

    RecentViewModel vm;
    WordAdapter adapter;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onMenuCreated(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onMenuCreated(menu, inflater);

        inflater.inflate(R.menu.menu_home, menu);
        BaseActivity activity = getParent();

        if (activity instanceof SearchViewCallback) {
            SearchViewCallback searchCallback = (SearchViewCallback) activity;
            searchView = searchCallback.getSearchView();
/*            searchView.setOnMicClickListener(new Search.OnMicClickListener() {
                @Override
                public void onMicClick() {

                }
            });*/
            //searchView.setLogo();
            //searchView.setVisibility(View.VISIBLE);

            MenuItem item = menu.findItem(R.id.item_search);
        }
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
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        vm.start();
    }

    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);
        if (searchView.isOpen()) {
            searchView.close();
        }
        vm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.refresh(!adapter.isEmpty(), adapter.isEmpty());
    }

    @Override
    public void onRefresh() {
        vm.refresh(!adapter.isEmpty(), true);
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
    public boolean onQueryTextChange(@NonNull String newText) {
        BaseFragment fragment = getCurrentFragment();
        return fragment != null && fragment.onQueryTextChange(newText);
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

    private void initView() {
        setTitle(R.string.home);

        binding = (FragmentHomeBinding) super.binding;
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_recent_words);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        vm = ViewModelProviders.of(this, factory).get(RecentViewModel.class);
        vm.setUiCallback(this);
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
            public void onScrollingAtEnd() {
                vm.update(false);
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

    private void processSuccess(List<WordItem> items) {
        if (scroller.isScrolling()) {
            return;
        }
        Timber.v("Recent result %s", items.size());
        adapter.addItems(items);
        AndroidUtil.getUiHandler().postDelayed(() -> processUiState(UiState.EXTRA), 1000);
    }

    private void processSingleSuccess(WordItem item) {
        adapter.updateSilently(item);
    }
}
