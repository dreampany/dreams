package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.databinding.FragmentCoinAlertBinding;
import com.dreampany.lca.databinding.FragmentDetailsBinding;
import com.dreampany.lca.ui.model.UiTask;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;

import javax.inject.Inject;

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
    }

    @Override
    protected void onStopUi() {

    }

    private void initView() {
        setTitle(R.string.alert);
        binding = (FragmentCoinAlertBinding) super.binding;
        UiTask<Coin> uiTask = getCurrentTask(true);
        Coin coin = uiTask.getInput();
        Timber.v("");
    }
}
