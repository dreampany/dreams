package com.dreampany.scan.ui.fragment;

import android.app.Activity;
import android.app.SearchManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.scan.R;
import com.dreampany.scan.data.enums.ScanType;
import com.dreampany.scan.data.model.Scan;
import com.dreampany.scan.databinding.FragmentHomeBinding;
import com.dreampany.scan.ui.adapter.ScanAdapter;
import com.dreampany.scan.ui.model.ScanItem;
import com.dreampany.scan.ui.model.UiTask;
import com.dreampany.scan.vm.ScanViewModel;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import hugo.weaving.DebugLog;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class FlagFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentHomeBinding binding;
    ScanViewModel viewModel;
    ScanAdapter adapter;

    @Inject
    public FlagFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_home;
    }

    @Override
    protected void onMenuCreated(@NonNull Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_FULLSCREEN);
            searchView.setQueryHint(getString(R.string.search));
            SearchManager searchManager = (SearchManager) searchView.getContext().getSystemService(Context.SEARCH_SERVICE);
            Activity activity = getActivity();
            if (searchManager != null && activity != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
                searchView.setOnQueryTextListener(this);
            }
        }
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
    }

    @Override
    protected void onStopUi() {
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loads(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.removeMultipleSubscription();
    }

    @Override
    public void onRefresh() {
        viewModel.loads(true);
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
            case R.id.fab:
                viewModel.loadScan(ScanType.TEXT, true);
                break;
        }
    }


    private void initView() {
        setTitle(R.string.home);
        binding = (FragmentHomeBinding) super.binding;
        binding.setLifecycleOwner(this);
        ViewUtil.setSwipe(binding.layoutRefresh, this);
        ViewUtil.setClickListener(this, R.id.fab);

        viewModel = ViewModelProviders.of(this, factory).get(ScanViewModel.class);
        UiTask<Scan> uiTask = getCurrentTask(true);
        viewModel.setTask(uiTask);
        // viewModel.getEvent().observe(this, this::processEvent);
        viewModel.getOutputs().observe(this, this::processResponse);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new ScanAdapter(this);
        ViewUtil.setRecycler(
                binding.recycler,
                adapter,
                new SmoothScrollLinearLayoutManager(getContext()),
                null,
                new FlexibleItemDecoration(getContext())
                        //   .addItemViewType(R.layout.item_coin, viewModel.getItemOffset())
                        .withEdge(true)
        );
    }

    @DebugLog
    private void processResponse(Response<List<ScanItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress progress = (Response.Progress) response;
            processProgress(progress.getLoading());
            return;
        }

        if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
            return;
        }

        if (response instanceof Response.Success) {
            Response.Success<List<ScanItem>> success = (Response.Success<List<ScanItem>>) response;
            processSuccess(success.getData());
        }
    }

    @DebugLog
    private void processProgress(boolean loading) {
        if (loading) {
            updateState(UiState.SHOW_PROGRESS);
        } else {
            updateState(UiState.HIDE_PROGRESS);
        }
    }

    @DebugLog
    private void processFailure(Throwable error) {
        if (error instanceof IOException) {
            updateState(UiState.OFFLINE);
        }
    }

    @DebugLog
    private void processSuccess(List<ScanItem> items) {
        adapter.addItems(items);
        if (adapter.isEmpty()) {
            updateState(UiState.EMPTY);
        } else  {
            updateState(UiState.CONTENT);
        }
    }

    private void updateState(UiState state) {
        if (viewModel.getUiState() == state) {
            return;
        }
        viewModel.setUiState(state);
        switch (state) {
            case SHOW_PROGRESS:
                binding.stateful.showContent();
                binding.layoutRefresh.setRefreshing(true);
                break;
            case HIDE_PROGRESS:
                binding.stateful.showContent();
                binding.layoutRefresh.setRefreshing(false);
                break;
            case ERROR:
                break;
            case OFFLINE:
                binding.layoutExpandable.expand();
                break;
            case ONLINE:
                binding.layoutExpandable.collapse();
                break;
            case CONTENT:
                binding.stateful.showContent();
                break;
        }
    }
}
