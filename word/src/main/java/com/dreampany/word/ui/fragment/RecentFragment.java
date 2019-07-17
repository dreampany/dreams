package com.dreampany.word.ui.fragment;

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
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.databinding.FragmentRecentBinding;
import com.dreampany.word.ui.activity.NavigationActivity;
import com.dreampany.word.ui.activity.ToolsActivity;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.dreampany.word.ui.enums.UiSubtype;
import com.dreampany.word.ui.enums.UiType;
import com.dreampany.word.ui.model.LoadItem;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.LoaderViewModel;
import com.dreampany.word.vm.RecentViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@ActivityScope
public class RecentFragment extends BaseMenuFragment implements SmartAdapter.Callback<WordItem> {

    private final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    FragmentRecentBinding binding;
    RecentViewModel vm;
    LoaderViewModel lvm;
    WordAdapter adapter;
    OnVerticalScrollListener scroller;
    SwipeRefreshLayout refresh;
    ExpandableLayout expandable;
    RecyclerView recycler;

    @Inject
    public RecentFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recent;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_recent;
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

    }

    @Override
    public void onResume() {
        super.onResume();
        lvm.loadSubtitle();
        //lvm.search();
        //recentVm.search(adapter.isEmpty());
    }

    @Override
    public void onPause() {
        vm.removeMultipleSubscription();
        vm.removeSingleSubscription();
        vm.removeUpdateDisposable();
        // recentVm.removeUpdateVisibleItemsDisposable();
        super.onPause();
    }

    @Override
    public void onRefresh() {
        //recentVm.search(true);
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
            case R.id.item_all:
                openAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.text_word:
                String text = ViewUtil.getText(v);
                vm.speak(text);
                break;
/*            case R.id.button_like:
                //recentVm.toggle((Word) v.getTag());
                break;*/
            case R.id.fab:
                openOcrUi();
                break;
        }
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


    private void initView() {
        setTitle(R.string.recent_words);
        binding = (FragmentRecentBinding) super.binding;
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_recent_words);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.fab);
        UiTask<Word> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(RecentViewModel.class);
        lvm = ViewModelProviders.of(this, factory).get(LoaderViewModel.class);
        vm.setTask(uiTask);
        vm.setUiCallback(this);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputs(this, this::processResponse);
        vm.observeOutput(this, this::processSingleResponse);
//        vm.observeFlag(this, this::onFlag);
        lvm.observeOutput(this, this::processLoadResponse);
        lvm.observeSubtitle(this, this::processSubtitle);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new WordAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener() {
            @Override
            public void onScrolling() {
                // recentVm.updateVisibleItemIf();
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
                    refresh.setRefreshing(true);
                }
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

    public void processSubtitle(String response) {
        setSubtitle(response);
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
            processSingleProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<WordItem> result = (Response.Result<WordItem>) response;
            processSingleSuccess(result.getData());
        }
    }


    public void processLoadResponse(Response<LoadItem> response) {
        if (response instanceof Response.Result) {
            Response.Result<LoadItem> result = (Response.Result<LoadItem>) response;
            processSuccess(result.getData());
        }
    }


    private void processSuccess(LoadItem item) {
        Timber.v("Load Progress = %d", item.getItem().getTotal());
        String title = TextUtil.getString(getContext(), R.string.total_words, item.getItem().getCurrent());
        setSubtitle(title);
    }

    public void onFlag(WordItem item) {
        adapter.updateSilently(item);
    }

    private void processProgress(boolean loading) {
        if (loading) {
            vm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            vm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }

    private void processSingleProgress(boolean loading) {
        if (loading) {
            //binding.progress.setVisibility(View.VISIBLE);
        } else {
            //binding.progress.setVisibility(View.GONE);
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
        UiTask<Word> task = new UiTask<>(false, UiType.WORD, UiSubtype.VIEW);
        task.setInput(item);
        openActivity(ToolsActivity.class, task);
    }

    private void openOcrUi() {
        hideAlert();
        NavigationActivity parent = (NavigationActivity) activityCallback.getUiActivity();
//        parent.setSelectedItem(R.id.item_ocr);
    }

    private void openAll() {
        UiTask<Word> task = new UiTask<>(true, UiType.WORD, UiSubtype.RECENTS);
        openActivity(ToolsActivity.class, task);
    }

    @Nullable
    @Override
    public List<WordItem> getItems() {
        return null;
    }

    @Override
    public boolean getEmpty() {
        return false;
    }

/*    private void openSheet() {
        new SheetMenu().apply(it -> {
            it.setMenu(R.menu.menu_add);
            it.setTitle(TextUtil.getString(getContext(), R.string.add_word_title));
            it.setAutoCancel(true);
            it.setClick(item -> {
                switch (item.getItemId()) {
                    case R.id.item_add:
                        //todo add manual word ui
                        return true;
                    case R.id.item_camera:
                        //todo open camera
                        openCameraOcr();
                        return true;
                    case R.id.item_image:
                        //todo opoen to choose image
                        openImageOcr();
                        return true;
                }
                return false;
            });
        }).show(getContext());
    }*/

/*    private void openTextOcr() {
        UiTask<Word> task = new UiTask<>();
        task.setUiType(UiType.OCR);
        task.setSubtype(UiSubtype.TEXT);
        openActivity(ToolsActivity.class, task, REQUEST_OCR);
    }*/



/*    private void openImageOcr() {
        UiTask<Word> task = new UiTask<>();
        task.setUiType(UiType.OCR);
        task.setSubtype(UiSubtype.IMAGE);
        task.setFull(true);
        //openActivity(ToolsActivity.class, task);
    }*/
}
