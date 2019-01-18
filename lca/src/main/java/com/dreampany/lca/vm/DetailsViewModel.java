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
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.repository.CoinRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.NetworkManager;
import com.dreampany.network.data.model.Network;
import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class DetailsViewModel extends BaseViewModel<Coin, CoinItem, UiTask<Coin>> {

    private static final long initialDelay = 0L;
    private static final long period = Constants.Period.INSTANCE.getCoinDetails();
    private static final int RETRY_COUNT = 3;

    private final NetworkManager network;
    private final CoinRepository repo;
    private Disposable updateDisposable;

    @Inject
    DetailsViewModel(Application application,
                     RxMapper rx,
                     AppExecutors ex,
                     ResponseMapper rm,
                     NetworkManager network,
                     CoinRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        removeUpdateDisposable();
        super.clear();
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    @DebugLog
    void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.isConnected()) {
                state = UiState.ONLINE;
                Response<List<CoinItem>> result = getOutputs().getValue();
                if (result instanceof Response.Failure) {
                    //getEx().postToUi(() -> loads(false), 250L);
                }
                //getEx().postToUi(this::update, 2000L);
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
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(result -> {
                    postResult(result, true);
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void toggle(Coin coin) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(coin))
                .subscribe(result -> postResult(result, true), this::postFailure);
        addSingleSubscription(disposable);
    }

/*    @DebugLog
    public void update() {
        if (hasDisposable(updateDisposable)) {
            return;
        }
        updateDisposable = getRx()
                .backToMain(updateItemsIntervalRx())
                .subscribe(result -> postResult(result, false), this::postFailure);
        addSubscription(updateDisposable);
    }*/

/*    private Flowable<List<CoinItem>> updateItemsIntervalRx() {
        return Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> {
                    Coin item = Objects.requireNonNull(getTask()).getInput();
                    Timber.d("Next Item to update %s", item);
                    return updateItemsRx(item).blockingGet();
                }).retry(RETRY_COUNT);
    }*/

    @DebugLog
    private Maybe<List<CoinItem>> updateItemsRx(Coin item, Currency[] currencies) {
        return repo.getItemRx(CoinSource.CMC, item.getSymbol(),  currencies, true).map(coin -> getItemsRx(coin).blockingGet());
    }

    private Maybe<List<CoinItem>> getItemsRx() {
        Coin coin = Objects.requireNonNull(getTask()).getInput();
        return Maybe.zip(
                getDetailsCoinItem(coin),
                getQuoteCoinItem(coin, Currency.USD),
                (left, right) -> Arrays.asList(left, right));
    }

    private Maybe<List<CoinItem>> getItemsRx(Coin coin) {
        return Maybe.zip(
                getDetailsCoinItem(coin),
                getQuoteCoinItem(coin, Currency.USD),
                (left, right) -> Arrays.asList(left, right));
    }

    private Maybe<CoinItem> getDetailsCoinItem(Coin coin) {
        return Maybe.fromCallable(
                () -> {
                    CoinItem item = CoinItem.getDetailsItem(coin);
                    boolean flagged = repo.isFlagged(coin);
                    item.setFlagged(flagged);
                    return item;
                });
    }

    private Maybe<CoinItem> getQuoteCoinItem(Coin coin, Currency currency) {
        return Maybe.fromCallable(() -> {
            CoinItem item = CoinItem.getQuoteItem(coin, currency);
            adjustFlag(coin, item);
            return item;
        });
    }

    private Maybe<CoinItem> toggleImpl(Coin coin) {
        return Maybe.fromCallable(() -> {
            repo.toggleFlag(coin);
            return getItemRx(coin);
        });
    }

    private CoinItem getItemRx(Coin coin) {
        SmartMap<Long, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            item = CoinItem.getDetailsItem(coin);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        adjustFlag(coin, item);
        return item;
    }

    private void adjustFlag(Coin coin, CoinItem item) {
        boolean flagged = repo.isFlagged(coin);
        item.setFlagged(flagged);
    }
}
