package com.dreampany.share.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.NetworkManager;
import com.dreampany.share.data.model.User;
import com.dreampany.share.data.source.repository.nearby.NearbyRepository;
import com.dreampany.share.ui.model.UiTask;
import com.dreampany.share.ui.model.UserItem;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 9/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class UserViewModel extends BaseViewModel<User, UserItem, UiTask<User>> {

    private final NetworkManager network;
    private final NearbyRepository repo;

    @Inject
    UserViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  NearbyRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
    }


}
