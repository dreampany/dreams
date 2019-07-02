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
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.databinding.ContentRecyclerBinding;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.ui.listener.OnVerticalScrollListener;
import com.dreampany.frame.util.ViewUtil;
import com.dreampany.network.data.model.Network;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import timber.log.Timber;


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
        userVm.start();
    }

    @Override
    protected void onStopUi() {
        processUiState(UiState.HIDE_PROGRESS);
        userVm.start();
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
        userVm.observeOutputsOfNetwork(this, this::processResponseOfNetwork);
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

    public void processResponseOfNetwork(Response<List<Network>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<Network>> result = (Response.Result<List<Network>>) response;
            processSuccessOfNetwork(result.getType(), result.getData());
        }
    }

    private void processProgress(boolean loading) {
        if (loading) {
            userVm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            userVm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }

    private void processFailure(Throwable error) {
        if (error instanceof IOException || error.getCause() instanceof IOException) {
            userVm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            userVm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            userVm.updateUiState(UiState.EXTRA);
        } else if (error instanceof MultiException) {
            for (Throwable e : ((MultiException) error).getErrors()) {
                processFailure(e);
            }
        }
    }

    private void processSuccessOfNetwork(Response.Type type, List<Network> items) {
        Timber.v("Result Type[%s] Size[%s]", type.name(), items.size());

    }
}
