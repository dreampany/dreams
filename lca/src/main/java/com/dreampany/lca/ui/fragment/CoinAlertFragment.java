package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.lca.R;
import org.jetbrains.annotations.Nullable;

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
    }
}
