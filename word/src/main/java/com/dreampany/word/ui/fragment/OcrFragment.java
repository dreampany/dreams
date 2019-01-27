package com.dreampany.word.ui.fragment;

import android.app.Activity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.databinding.FragmentOcrBinding;
import com.dreampany.word.ui.activity.ToolsActivity;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.dreampany.word.ui.enums.UiSubtype;
import com.dreampany.word.ui.enums.UiType;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.vm.OcrViewModel;
import com.dreampany.word.vm.TextOcrViewModel;

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
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 10/2/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class OcrFragment extends BaseMenuFragment implements SmartAdapter.Callback<WordItem> {

    private final int REQUEST_OCR = 101;
    private final String EMPTY = "empty";

    @Inject
    ViewModelProvider.Factory factory;
    FragmentOcrBinding binding;
    OcrViewModel vm;
    TextOcrViewModel tvm;
    WordAdapter adapter;
    OnVerticalScrollListener scroller;
    SwipeRefreshLayout refresh;
    ExpandableLayout expandable;
    RecyclerView recycler;

    @Inject
    public OcrFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ocr;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_search;
    }

    @Override
    public int getSearchMenuItemId() {
        return R.id.item_search;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        openTextOcr();
    }

    @Override
    protected void onStopUi() {

    }

    @Override
    public void onResume() {
        super.onResume();
        vm.loads(adapter.isEmpty());
    }

/*    @Override
    public void onPause() {
        vm.removeMultipleSubscription();
        vm.removeSingleSubscription();
        vm.removeUpdateDisposable();
        vm.removeUpdateVisibleItemsDisposable();
        tvm.clearInputs();
        super.onPause();
    }*/

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
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.fab:
                openTextOcr();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_OCR) {
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            Timber.w("Failed in %d request code", REQUEST_OCR);
            return;
        }

        UiTask<Word> task = getCurrentTask(data);
        String text = task.getComment();
        Timber.d("Detected text %s", text);
        tvm.loadOcr(text);
    }

    @DebugLog
    private void initView() {
        setTitle(R.string.ocr_words);
        binding = (FragmentOcrBinding) super.binding;
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        ViewUtil.setText(this, R.id.text_empty, R.string.empty_ocr_words);

        refresh = binding.layoutRefresh;
        expandable = binding.layoutTopStatus.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        ViewUtil.setSwipe(refresh, this);
        ViewUtil.setClickListener(this, R.id.fab);
        UiTask<Word> uiTask = getCurrentTask(true);
        vm = ViewModelProviders.of(this, factory).get(OcrViewModel.class);
        tvm = ViewModelProviders.of(this, factory).get(TextOcrViewModel.class);
        vm.setTask(uiTask);
        vm.setUiCallback(this);
        vm.observeUiState(this, this::processUiState);
        vm.observeOutputs(this, this::processResponse);
        vm.observeOutput(this, this::processSingleResponse);
        vm.observeFlag(this, this::onFlag);
        tvm.observeOutputs(this, this::processOcrResponse);
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
            binding.progress.setVisibility(View.VISIBLE);
        } else {
            binding.progress.setVisibility(View.GONE);
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

    public void processOcrResponse(Response<List<WordItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            boolean loading = result.getLoading();
            if (loading) {
                showProgress(TextUtil.getString(getContext(), R.string.validate_words));
            } else {
                hideProgress();
            }

        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            Throwable error = result.getError();
            Timber.e(error);

        } else if (response instanceof Response.Result) {
            Response.Result<List<WordItem>> result = (Response.Result<List<WordItem>>) response;
            List<WordItem> items = result.getData();

            String title = TextUtil.getString(getContext(), R.string.ocr_detected_word_title);
            String info = TextUtil.getString(getContext(), R.string.ocr_detected_no_word_info);
            int backColor = R.color.material_red600;

            if (!DataUtil.isEmpty(items)) {
                vm.loads(true);
                info = TextUtil.getString(getContext(), R.string.ocr_detected_word_info, items.size());
                backColor = R.color.colorAccent;
            }
            showAlert(title, info, backColor, 5000L, v -> {
            });
        }
    }

    private void openUi(Word item) {
        UiTask<Word> task = new UiTask<>(false);
        task.setInput(item);
        task.setUiType(UiType.WORD);
        task.setSubtype(UiSubtype.VIEW);
        openActivity(ToolsActivity.class, task);
    }

    private void openTextOcr() {
        //vm.clearInputs();
        UiTask<Word> task = new UiTask<>();
        task.setUiType(UiType.OCR);
        task.setSubtype(UiSubtype.TEXT);
        openActivity(ToolsActivity.class, task, REQUEST_OCR);
    }

    @Nullable
    @Override
    public List<WordItem> getItems() {
        return null;
    }
}
