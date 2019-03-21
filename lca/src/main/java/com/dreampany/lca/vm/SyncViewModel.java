package com.dreampany.lca.vm;


import android.app.Application;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.pref.LoadPref;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.manager.NetworkManager;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class SyncViewModel
        extends BaseViewModel<Coin, CoinItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private final NetworkManager network;
    private final LoadPref pref;
    private final ApiRepository repo;
    private int coinIndex;
    private boolean syncCompleted;

    @Inject
    SyncViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  LoadPref pref,
                  ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
        coinIndex = Constants.Limit.COIN_START_INDEX;
    }

    @Override
    public void clear() {
        network.deObserve(this, true);
        super.clear();
    }

    @Override
    public void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.hasInternet()) {
                state = UiState.ONLINE;
                Response<List<CoinItem>> result = getOutputs().getValue();
                if (!syncCompleted && result == null || result instanceof Response.Failure) {
                    //getEx().postToUi(this::loads, 250L);
                }
            }
        }
        UiState finalState = state;
        //getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void start() {
        network.observe(this, true);
    }

    public void loads() {
        if (!preLoads(false)) {
            return;
        }
        int limit = Constants.Limit.COIN_PAGE;
        Currency currency = Currency.USD;
        Disposable disposable = getRx()
                .backToMain(getListingRepeatRx())
                .subscribe(result -> {
                    postResult(Response.Type.GET, result);
                }, error -> {
                    postFailures(error);
                });
        addMultipleSubscription(disposable);
    }

    /* private api */
    private Flowable<List<CoinItem>> getListingRepeatRx() {
        return Flowable
                .create((FlowableOnSubscribe<List<CoinItem>>) emitter -> {
                    //getListingRx(coinPage - 1, Constants.Limit.COIN_PAGE, Currency.USD).blockingGet();
                }, BackpressureStrategy.BUFFER)
                .repeatWhen(handler -> handler.delay(5, TimeUnit.SECONDS))
                .takeWhile(new Predicate<List<CoinItem>>() {
                    @Override
                    public boolean test(List<CoinItem> coinItems) throws Exception {
                        if (DataUtil.isEmpty(coinItems)) {

                        }
                        return !syncCompleted && network.hasInternet();
                    }
                });
    }

    private Maybe<List<CoinItem>> getListingRx(int index, int limit, Currency currency) {
        return repo
                .getItemsIfRx(CoinSource.CMC, index, limit, currency)
                .flatMap((Function<List<Coin>, MaybeSource<List<CoinItem>>>) this::getItemsRx);
    }

    private Maybe<List<CoinItem>> getItemsRx(List<Coin> items) {
        return Maybe.create(emitter -> {
            List<CoinItem> result = getItems(items);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    private List<CoinItem> getItems(List<Coin> result) {
        List<Coin> coins = new ArrayList<>(result);
        List<Coin> ranked = new ArrayList<>();
        for (Coin coin : coins) {
            if (coin.getRank() > 0) {
                ranked.add(coin);
            }
        }

        Collections.sort(ranked, (left, right) -> left.getRank() - right.getRank());
        coins.removeAll(ranked);
        coins.addAll(0, ranked);

        //putFlags(coins, Constants.Limit.COIN_FLAG);
        List<CoinItem> items = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            CoinItem item = getItem(coin);
            items.add(item);
        }
        return items;
    }

    private void adjustFlag(Coin coin, CoinItem item) {
        boolean flagged = repo.isFavorite(coin);
        item.setFavorite(flagged);
    }

    private CoinItem getItem(Coin coin) {
        SmartMap<Long, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            //item = CoinItem.getSimpleItem(coin);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        adjustFlag(coin, item);
        return item;
    }

/*    private int getNextCoinIndex() {

        while (true) {
            long time = pref.getCoinListingTime(coinIndex);
            if (TimeUtil.isExpired(time, Constants.Time.INSTANCE.getListing())) {
                return coinIndex;
            }
            coinIndex++;
            AndroidUtil.sleep(10L);
        }
        return -1;
    }*/
}
