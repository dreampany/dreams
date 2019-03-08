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
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.network.data.model.Network;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinViewModel
        extends BaseViewModel<Coin, CoinItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private static final boolean OPEN = true;

    private final NetworkManager network;
    private final ApiRepository repo;
    private Disposable updateDisposable;
    private SmartAdapter.Callback<CoinItem> uiCallback;

    @Inject
    CoinViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
    }

    @Override
    public void clear() {
        network.deObserve(this, true);
        removeUpdateDisposable();
        super.clear();
    }

    @Override
    public void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.hasInternet()) {
                state = UiState.ONLINE;
                Response<List<CoinItem>> result = getOutputs().getValue();
                if (result instanceof Response.Failure) {
                    boolean empty = uiCallback == null || uiCallback.getEmpty();
                    getEx().postToUi(() -> loads(false, empty), 250L);
                }
                //getEx().postToUi(this::update, 2000L);
            }
        }
        UiState finalState = state;
        //getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void setUiCallback(SmartAdapter.Callback<CoinItem> callback) {
        this.uiCallback = callback;
    }

    public void start() {
        network.observe(this, true);
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void refresh(boolean onlyUpdate, boolean withProgress) {
        if (onlyUpdate) {
            update(withProgress);
            return;
        }
        loads(true, withProgress);
    }

    public void loads(boolean fresh, boolean withProgress) {
        if (!OPEN) {
            return;
        }
        if (!preLoads(fresh)) {
            return;
        }
        Currency currency = Currency.USD;
        Disposable disposable = getRx()
                .backToMain(getItemsRx(currency))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(result);
                }, error -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void update(boolean withProgress) {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateDisposable)) {
            return;
        }
        Currency currency = Currency.USD;
        updateDisposable = getRx()
                .backToMain(getItemsRx(currency))
                .subscribe(result -> {
                    postResult(result, withProgress);
                }, this::postFailure);
        addSubscription(updateDisposable);
    }

    public void toggleFavorite(Coin coin) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(coin))
                .subscribe(result -> postResult(result, false), this::postFailure);
        addSingleSubscription(disposable);
    }

    /* private api */
    private Maybe<List<CoinItem>> getItemsRx(Currency currency) {
        return Maybe.create(new MaybeOnSubscribe<List<CoinItem>>() {
            @Override
            public void subscribe(MaybeEmitter<List<CoinItem>> emitter) throws Exception {
                Coin coin = Objects.requireNonNull(getTask()).getInput();
                Coin result = repo.getItemIf(CoinSource.CMC, coin.getSymbol(), currency);
                List<CoinItem> items = null;
                if (result != null) {
                    getTask().setInput(result);
                    items = new ArrayList<>();
                    items.add(getDetailsCoinItem(result));
                    items.add(getQuoteCoinItem(result, currency));
                }

                if (emitter.isDisposed()) {
                    return;
                }
                if (DataUtil.isEmpty(items)) {
                    emitter.onError(new NullPointerException());
                } else {
                    emitter.onSuccess(items);
                }
            }
        });
    }

/*    private Maybe<List<CoinItem>> getItemsRx(Coin coin, Currency currency) {
        return Maybe.zip(
                getDetailsCoinItem(coin),
                getQuoteCoinItem(coin, currency),
                (left, right) -> Arrays.asList(left, right));
    }*/

    private CoinItem getDetailsCoinItem(Coin coin) {
        CoinItem item = CoinItem.getDetailsItem(coin);
        adjustFavorite(coin, item);
        return item;
    }

    private CoinItem getQuoteCoinItem(Coin coin, Currency currency) {
        CoinItem item = CoinItem.getQuoteItem(coin, currency);
        adjustFavorite(coin, item);
        return item;
    }

    private Maybe<CoinItem> toggleImpl(Coin coin) {
        return Maybe.fromCallable(() -> {
            repo.toggleFavorite(coin);
            return getItem(coin);
        });
    }

    private CoinItem getItem(Coin coin) {
        SmartMap<Long, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            item = CoinItem.getDetailsItem(coin);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        adjustFavorite(coin, item);
        return item;
    }

    private void adjustFavorite(Coin coin, CoinItem item) {
        boolean flagged = repo.isFavorite(coin);
        item.setFavorite(flagged);
    }
}
