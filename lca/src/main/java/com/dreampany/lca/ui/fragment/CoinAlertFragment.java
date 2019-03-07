package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.databinding.FragmentCoinAlertBinding;
import com.dreampany.lca.databinding.FragmentDetailsBinding;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.CoinAlertViewModel;
import com.dreampany.lca.vm.CoinViewModel;
import hugo.weaving.DebugLog;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by roman on 3/3/19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@ActivityScope
public class CoinAlertFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory factory;
    FragmentCoinAlertBinding binding;
    CoinAlertViewModel vm;

    @Inject
    public CoinAlertFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coin_alert;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        //loadView();
        UiTask<Coin> uiTask = getCurrentTask();
        vm.load(uiTask.getInput(), true);
    }

    @Override
    protected void onStopUi() {

    }

     void initView() {
        setTitle(R.string.alert);
        binding = (FragmentCoinAlertBinding) super.binding;

         vm = ViewModelProviders.of(this, factory).get(CoinAlertViewModel.class);
         //vm.observeUiState(this, this::processUiState);
         vm.observeOutput(this, this::processResponse);
    }

    public void processResponse(Response<CoinAlertItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<CoinAlertItem> result = (Response.Result<CoinAlertItem>) response;
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
        } else if (error instanceof ExtraException) {
            vm.updateUiState(UiState.EXTRA);
        }
    }

    @DebugLog
    private void processSuccess(CoinAlertItem item) {

    }
}
