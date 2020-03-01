package com.dreampany.word.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dreampany.framework.data.enums.UiState;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.ActivityScope;
import com.dreampany.framework.misc.exceptions.EmptyException;
import com.dreampany.framework.misc.exceptions.ExtraException;
import com.dreampany.framework.misc.exceptions.MultiException;
import com.dreampany.framework.ui.fragment.BaseMenuFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.AndroidUtil;
import com.dreampany.framework.util.DataUtil;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.databinding.FragmentSearchBinding;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.dreampany.word.ui.model.WordItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import cz.kinst.jakub.view.StatefulLayout;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;


/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@ActivityScope
public class SearchFragment extends BaseMenuFragment {

    private static final String SEARCH = "search";
    private static final String SEARCH_EMPTY = "search_empty";

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentSearchBinding binding;
    //private SearchViewModel vm;
    private WordAdapter adapter;
    private OnVerticalScrollListener scroller;
    private RecyclerView recycler;
    private final StringBuilder query = new StringBuilder();

    @Inject
    public SearchFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
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
    }

    @Override
    protected void onStopUi() {
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
         fireQuery(newText, false);
         return true;
    }

    @Override
    public boolean onQueryTextSubmit(@NotNull String query) {
        fireQuery(query, true);
        return true;
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.text_word:
                String text = ViewUtil.getText(v);
                //vm.speak(text);
                break;
/*            case R.id.button_like:
                vm.toggle((Word) v.getTag());
                break;*/
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


    private void initView() {
        setTitle(R.string.search_words);
        binding = (FragmentSearchBinding) super.binding;
        binding.stateful.setStateView(SEARCH, LayoutInflater.from(getContext()).inflate(R.layout.item_search, null));
        binding.stateful.setStateView(SEARCH_EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_search_empty, null));

        recycler = binding.layoutRecycler.recycler;

       // vm = ViewModelProviders.of(this, factory).get(SearchViewModel.class);
       // vm.observeUiState(this, this::processUiState);
       // vm.observeOutputs(this, this::processResponse);
//        vm.observeFlag(this, this::onFlag);
        binding.stateful.setState(SEARCH);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new WordAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener() {
            @Override
            public void onScrolling() {
            }
        };
        //adapter.setEndlessScrollListener(this, CoinItem.getProgressItem());
        ViewUtil.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(getContext()),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_word, 0)
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
  //              binding.progress.setVisibility(View.VISIBLE);
                break;
            case HIDE_PROGRESS:
//                binding.progress.setVisibility(View.GONE);
                break;
            case EXTRA:
                processUiState(adapter.isEmpty() ? UiState.EMPTY : UiState.CONTENT);
                break;
            case EMPTY:
            case ERROR:
                binding.stateful.setState(SEARCH_EMPTY);
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

    public void onFlag(WordItem item) {
        adapter.updateSilently(item);
    }

    private void processProgress(boolean loading) {
        if (loading) {
            //vm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
           // vm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }

    private void processFailure(Throwable error) {
        if (error instanceof IOException || error.getCause() instanceof IOException) {
            //vm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            //vm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
           // vm.updateUiState(UiState.EXTRA);
        } else if (error instanceof MultiException) {
            for (Throwable e : ((MultiException) error).getErrors()) {
                processFailure(e);
            }
        }
    }

    private void processSuccess(List<WordItem> items) {
        if (DataUtil.isEmpty(items)) {
            processUiState(UiState.EMPTY);
            return;
        }
        if (scroller.isScrolling()) {
            return;
        }
        recycler.setNestedScrollingEnabled(false);
        adapter.addItemsBySearch(items);
        recycler.setNestedScrollingEnabled(true);
        processUiState(UiState.EXTRA);
    }

    private void openUi(Word item) {
/*        UiTask<Word> task = new UiTask<Word>(false, UiType.WORD, UiSubtype.VIEW);
        task.setInput(item);
        openActivity(ToolsActivity.class, task);*/
    }

    private void fireQuery(String query, boolean urgent) {
        if (urgent) {
//            AndroidUtil.getUiHandler().postDelayed(() -> KeyboardUtils.hideSoftInput(getParent()), 1000);
        }
        this.query.setLength(0);
        this.query.append(query);
        AndroidUtil.Companion.getUiHandler().removeCallbacks(queryRunner);
        AndroidUtil.Companion.getUiHandler().postDelayed(queryRunner, urgent ? 0L : 1500L);
    }

    private final Runnable queryRunner = new Runnable() {
        @Override
        public void run() {
            if (query.length() > 0) {
                //vm.search(query.toString(), false);
            } else {
                //vm.updateUiState(UiState.EMPTY);
            }
        }
    };
}
