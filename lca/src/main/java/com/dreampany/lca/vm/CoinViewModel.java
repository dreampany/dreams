package com.dreampany.lca.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.NetworkManager;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/11/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class CoinViewModel extends BaseViewModel<Coin, CoinItem, UiTask<Coin>> {

        @Inject
        CoinViewModel(@NotNull Application application,
                      @NotNull RxMapper rx,
                      @NotNull AppExecutors ex,
                      @NotNull ResponseMapper rm,
                      @NotNull NetworkManager network) {
                super(application, rx, ex, rm);
        }
}
