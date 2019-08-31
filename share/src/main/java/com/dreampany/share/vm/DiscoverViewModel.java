package com.dreampany.share.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.ui.vm.BaseViewModel;
import com.dreampany.media.data.model.Media;
import com.dreampany.network.NetworkManager;
import com.dreampany.share.data.source.repository.nearby.NearbyRepository;
import com.dreampany.share.ui.model.MediaItem;
import com.dreampany.share.ui.model.UiTask;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class DiscoverViewModel extends BaseViewModel<Media, MediaItem, UiTask<Media>> {

    private final NetworkManager network;
    private final NearbyRepository repo;

    @Inject
    DiscoverViewModel(Application application,
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
