package com.dreampany.manager.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.ui.vm.BaseViewModel;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.manager.data.model.Demo;
import com.dreampany.manager.ui.model.DemoItem;
import com.dreampany.manager.ui.model.UiTask;

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