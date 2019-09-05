package com.dreampany.crime.vm;

import android.app.Application;

import com.dreampany.crime.data.model.Crime;
import com.dreampany.crime.ui.model.DemoItem;
import com.dreampany.crime.ui.model.UiTask;
import com.dreampany.framework.api.network.NetworkManager;
import com.dreampany.framework.data.enums.NetworkEvent;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.ui.vm.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CrimeViewModel extends BaseViewModel<Crime, DemoItem, UiTask<Crime>> {

    private final Set<Crime> uiFlags;

    @Inject
    CrimeViewModel(@NotNull Application application,
                   @NotNull ResponseMapper responseMapper,
                   @NotNull RxMapper rxMapper,
                   @NotNull AppExecutors executors,
                   @NotNull NetworkManager network) {
        super(application, responseMapper, rxMapper, executors, network);
        uiFlags = Collections.synchronizedSet(new HashSet<>());
        if (hasNetCheck()) {
            network.checkInternet();
        }
    }

    @Override
    public boolean hasNetCheck() {
        return false;
    }

    @Override
    public void onNetworkEvent(@NotNull NetworkEvent event) {
        if (event == NetworkEvent.ONLINE) {
            Response<List<DemoItem>> result = getOutputs().getValue();
            if (result instanceof Response.Failure) {
                //loads(true);
            }
        }
    }

    @NotNull
    @Override
    protected Flowable<String> getTitle() {
        return null;
    }

    @NotNull
    @Override
    protected Flowable<String> getSubtitle() {
        return null;
    }
}
