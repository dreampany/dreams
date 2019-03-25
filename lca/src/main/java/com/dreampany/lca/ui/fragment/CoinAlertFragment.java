package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.ui.fragment.BaseMenuFragment;
import com.dreampany.frame.util.FrescoUtil;
import com.dreampany.frame.util.NotifyUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.model.Quote;
import com.dreampany.lca.databinding.FragmentCoinAlertBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.CoinAlertViewModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by roman on 3/3/19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@ActivityScope
public class CoinAlertFragment extends BaseMenuFragment {

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
    public int getMenuId() {
        return R.menu.menu_coin_alert;
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Screen.coinAlert(getAppContext());
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_done:
                saveAlert();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void initView() {
        setTitle(R.string.alert);
        binding = (FragmentCoinAlertBinding) super.binding;

        vm = ViewModelProviders.of(this, factory).get(CoinAlertViewModel.class);
        //vm.observeUiState(this, this::processUiState);
        vm.observeOutput(this, this::processResponse);
    }

    void processResponse(Response<CoinAlertItem> response) {
        if (response instanceof Response.Progress) {
            Response.Progress result = (Response.Progress) response;
            processProgress(result.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure result = (Response.Failure) response;
            processFailure(result.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<CoinAlertItem> result = (Response.Result<CoinAlertItem>) response;
            processResult(result.getType(), result.getData());
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

    private void processResult(Response.Type type, CoinAlertItem item) {
        if (type == Response.Type.ADD) {
            NotifyUtil.showInfo(getContext(), R.string.message_alert_saved);
            return;
        }

        Coin coin = item.getCoin();
        String imageUrl = String.format(Locale.ENGLISH, Constants.ImageUrl.CoinMarketCapImageUrl, coin.getCoinId());
        FrescoUtil.loadImage(binding.imageIcon, imageUrl, true);

        String nameText = String.format(Locale.ENGLISH, getString(R.string.full_name), coin.getSymbol(), coin.getName());
        binding.textName.setText(nameText);

        Quote quote = coin.getUsdQuote();
        if (quote != null) {
            binding.textPrice.setText(String.format(getString(R.string.usd_format), quote.getPrice()));
        }

        if (!item.isEmpty()) {
            CoinAlert alert = item.getItem();
            if (alert.hasPriceUp()) {
                binding.editPriceUp.setText(String.format(getString(R.string.currency_format), alert.getPriceUp()));
                binding.checkUp.setChecked(true);
            } else {
                binding.editPriceUp.setText(String.format(getString(R.string.currency_format), quote.getPrice()));
            }
            if (alert.hasPriceDown()) {
                binding.editPriceDown.setText(String.format(getString(R.string.currency_format), alert.getPriceDown()));
                binding.checkDown.setChecked(true);
            } else {
                binding.editPriceDown.setText(String.format(getString(R.string.currency_format), quote.getPrice()));
            }
        } else {
            binding.editPriceUp.setText(String.format(getString(R.string.currency_format), quote.getPrice()));
            binding.editPriceDown.setText(String.format(getString(R.string.currency_format), quote.getPrice()));
        }

    }

    private void saveAlert() {
        if (!binding.checkUp.isChecked() && !binding.checkDown.isChecked()) {
            NotifyUtil.showInfo(getContext(), R.string.message_select_alert);
            return;
        }

        UiTask<Coin> task = getCurrentTask();
        Coin coin = task.getInput();

        double priceUp = 0, priceDown = 0;
        if (binding.checkUp.isChecked()) {
            try {
                priceUp = Double.parseDouble(binding.editPriceUp.getText().toString());
            } catch (NumberFormatException e) {
                binding.editPriceUp.setError(getString(R.string.error_price_up_input));
                return;
            }
        }

        if (binding.checkDown.isChecked()) {
            try {
                priceDown = Double.parseDouble(binding.editPriceDown.getText().toString());
            } catch (NumberFormatException e) {
                binding.editPriceDown.setError(getString(R.string.error_price_down_input));
                return;
            }
        }

        vm.save(coin, priceUp, priceDown, true);
    }
}
