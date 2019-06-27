package com.dreampany.cast.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dreampany.cast.R;
import com.dreampany.cast.databinding.FragmentHomeBinding;
import com.dreampany.cast.ui.adapter.UserAdapter;
import com.dreampany.cast.vm.UserViewModel;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.databinding.ContentRecyclerBinding;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.ViewUtil;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;


/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class HomeFragment extends BaseMenuFragment {

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentHomeBinding bindHome;
    // private ContentTopStatusBinding bindStatus;
    private ContentRecyclerBinding bindRecycler;

    private OnVerticalScrollListener scroller;

    private UserAdapter adapter;
    private UserViewModel userVm;

    @Inject
    public HomeFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
    }

    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);

    }

    private void initView() {
        setTitle(R.string.home);
        bindHome = (FragmentHomeBinding) super.binding;
       /* bindStatus = bindHome.layoutTopStatus;
        bindRecycler = binding.layoutRecycler;
        bindFullWord = binding.layoutFullWord;
        bindWord = bindFullWord.layoutWord;
        bindDef = bindFullWord.layoutDefinition;*/

/*        binding.stateful.setStateView(SEARCH, LayoutInflater.from(getContext()).inflate(R.layout.item_search, null));
        binding.stateful.setStateView(EMPTY, LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null));
        processUiState(UiState.SEARCH);
        //ViewUtil.setText(this, R.id.text_empty, R.string.empty_search_words);

        ViewUtil.setSwipe(binding.layoutRefresh, this);
        bindDef.toggleDefinition.setOnClickListener(this);
        bindWord.buttonFavorite.setOnClickListener(this);
        bindWord.textWord.setOnClickListener(this);
        bindWord.imageSpeak.setOnClickListener(this);
        binding.fab.setOnClickListener(this);*/

        userVm = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        userVm.observeUiState(this, this::processUiState);
/*
        searchVm.setUiCallback(this);
        searchVm.observeUiState(this, this::processUiState);
        searchVm.observeOutputsOfString(this, this::processResponseOfString);
        searchVm.observeOutputs(this, this::processResponse);
        searchVm.observeOutput(this, this::processSingleResponse);*/
    }

    private void initRecycler() {
        bindHome.setItems(new ObservableArrayList<>());
        adapter = new UserAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener() {
            @Override
            public void onScrollingAtEnd() {

            }
        };
        ViewUtil.setRecycler(
                adapter,
                bindRecycler.recycler,
                new SmoothScrollLinearLayoutManager(getContext()),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_user, userVm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                if (!bindHome.layoutRefresh.isRefreshing()) {
                    bindHome.layoutRefresh.setRefreshing(true);
                }
                break;
            case HIDE_PROGRESS:
                if (bindHome.layoutRefresh.isRefreshing()) {
                    bindHome.layoutRefresh.setRefreshing(false);
                }
                break;
            case OFFLINE:
               // bindStatus.layoutExpandable.expand();
                break;
            case ONLINE:
             //   bindStatus.layoutExpandable.collapse();
                break;
            case EXTRA:
                processUiState(adapter.isEmpty() ? UiState.EMPTY : UiState.CONTENT);
                break;
            case SEARCH:
                //binding.stateful.setState(SEARCH);
                break;
            case EMPTY:
             //   binding.stateful.setState(SEARCH);
                break;
            case ERROR:
                break;
            case CONTENT:
              //  binding.stateful.setState(StatefulLayout.State.CONTENT);
                break;
        }
    }
}
