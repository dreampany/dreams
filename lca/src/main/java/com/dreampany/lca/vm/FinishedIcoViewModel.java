package com.dreampany.lca.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.ui.vm.BaseViewModel;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.data.source.repository.IcoRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.IcoItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.network.data.model.Network;
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
public class FinishedIcoViewModel
        extends BaseViewModel<Ico, IcoItem, UiTask<Ico>>
        implements NetworkManager.Callback {

    private static final int LIMIT = Constants.Limit.ICO;

    private final NetworkManager network;
    private final IcoRepository repo;

    private SmartAdapter.Callback<IcoItem> uiCallback;

    @Inject
    FinishedIcoViewModel(Application application,
                         RxMapper rx,
                         AppExecutors ex,
                         ResponseMapper rm,
                         NetworkManager network,
                         IcoRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
    }

    @Override
    public void clear() {
        network.deObserve(this);
//        repo.clear(IcoStatus.FINISHED);
        super.clear();
    }

    @Override
    public void onNetworkResult(@NonNull List<Network> networks) {

        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.getInternet()) {
                state = UiState.ONLINE;
                Response<List<IcoItem>> result = getOutputs().getValue();
                if (result == null || result instanceof Response.Failure) {
                    boolean empty = uiCallback == null || uiCallback.getEmpty();
                    getEx().postToUi(() -> loads(false, empty), 250L);
                }
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void setUiCallback(SmartAdapter.Callback<IcoItem> callback) {
        this.uiCallback = callback;
    }

    public void start() {
        network.observe(this, true);
    }

    public void loads(boolean important, boolean progress) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.ADD,result);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    private Maybe<List<IcoItem>> getItemsRx() {
        return repo
                .getFinishedItemsRx(LIMIT)
                .flatMap((Function<List<Ico>, MaybeSource<List<IcoItem>>>) this::getItemsRx);
    }

    private Maybe<List<IcoItem>> getItemsRx(List<Ico> icos) {
        return Flowable.fromIterable(icos)
                .map(this::getItem)
                .toList()
                .toMaybe();
    }

    private IcoItem getItem(Ico ico) {
        SmartMap<String, IcoItem> map = getUiMap();
        IcoItem item = map.get(ico.getId());
        if (item == null) {
            item = IcoItem.getItem(ico);
            map.put(ico.getId(), item);
        }
        item.setItem(ico);
        return item;
    }
}
