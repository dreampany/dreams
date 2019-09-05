package com.dreampany.match.vm;

import android.app.Application;

import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.ui.vm.BaseViewModel;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.match.data.model.Demo;
import com.dreampany.match.ui.model.DemoItem;
import com.dreampany.match.ui.model.UiTask;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class DemoViewModel extends BaseViewModel<Demo, DemoItem, UiTask<Demo>> {

    private final NetworkManager network;

    @Inject
    DemoViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network) {
        super(application, rx, ex, rm);
        this.network = network;
    }
}