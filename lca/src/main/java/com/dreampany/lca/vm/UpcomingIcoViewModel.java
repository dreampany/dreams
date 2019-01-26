package com.dreampany.lca.vm;

import android.app.Application;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.data.source.repository.IcoRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.IcoItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.NetworkManager;
import com.dreampany.network.data.model.Network;
import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class UpcomingIcoViewModel extends BaseViewModel<Ico, IcoItem, UiTask<Ico>> {

    private static final int LIMIT = Constants.Limit.ICO;

    private final NetworkManager network;
    private final IcoRepository repo;

    @Inject
    UpcomingIcoViewModel(Application application,
                         RxMapper rx,
                         AppExecutors ex,
                         ResponseMapper rm,
                         NetworkManager network,
                         IcoRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        //network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        repo.clear(IcoStatus.UPCOMING);
        super.clear();
    }

    @DebugLog
    void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.isConnected()) {
                state = UiState.ONLINE;
                Response<List<IcoItem>> result = getOutputs().getValue();
                if (result instanceof Response.Failure) {
                    getEx().postToUi(() -> loads(false), 250L);
                }
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    @DebugLog
    public void loads(boolean fresh) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(result -> postResult(result, true), error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

/*    private Flowable<List<IcoItem>> getItemsInterval() {
        return Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .onErrorResumeNext(Flowable.empty())
                .map(tick -> getItemsRx().blockingGet());
    }*/

    private Maybe<List<IcoItem>> getItemsRx() {
        return repo
                .getUpcomingItemsRx(LIMIT)
                .flatMap((Function<List<Ico>, MaybeSource<List<IcoItem>>>) this::getItemsRx);
    }

    private Maybe<List<IcoItem>> getItemsRx(List<Ico> icos) {
        return Flowable.fromIterable(icos)
                .map(this::getItem)
                .toList()
                .toMaybe();
    }

    private IcoItem getItem(Ico ico) {
        SmartMap<Long, IcoItem> map = getUiMap();
        IcoItem item = map.get(ico.getId());
        if (item == null) {
            item = IcoItem.getItem(ico);
            map.put(ico.getId(), item);
        }
        item.setItem(ico);
        return item;
    }
}
