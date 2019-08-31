package com.dreampany.share.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.ui.vm.BaseViewModel;
import com.dreampany.network.NetworkManager;
import com.dreampany.network.data.model.Network;
import com.dreampany.share.ui.model.NetworkItem;
import com.dreampany.share.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class NetworkViewModel extends BaseViewModel<Network, NetworkItem, UiTask<Network>> {

    private final NetworkManager network;

    @Inject
    NetworkViewModel(@NotNull Application application,
                     @NotNull RxMapper rx,
                     @NotNull AppExecutors ex,
                     @NotNull ResponseMapper rm,
                     @NotNull NetworkManager network) {
        super(application, rx, ex, rm);
        this.network = network;
    }


    public void loads(boolean fresh) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .subscribe(this::postResult, this::postFailureMultiple);
        addMultipleSubscription(disposable);
    }

    private Flowable<List<NetworkItem>> getItemsRx() {
        return getItemsRx(network.getActiveNetworks());
    }


    private Flowable<List<NetworkItem>> getItemsRx(List<Network> result) {
        return Flowable.fromCallable(() -> {
            List<NetworkItem> items = new ArrayList<>(result.size());

            for (Network network : result) {
                NetworkItem item = getItem(network);
                items.add(item);
            }
            return items;
        });
    }

    private NetworkItem getItem(Network network) {
/*        SmartMap<Long, MediaItem> map = getUiMap();
        MediaItem item = map.get(apk.getId());
        if (item == null) {
            item = MediaItem.getItem(apk);
            map.put(apk.getId(), item);
        }*/
        return NetworkItem.getItem(network);
    }
}
