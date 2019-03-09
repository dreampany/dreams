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
import com.dreampany.lca.api.cmc.enums.CmcCurrency;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Exchange;
import com.dreampany.lca.data.source.repository.ExchangeRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.misc.CurrencyFormatter;
import com.dreampany.lca.ui.enums.TimeType;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.ExchangeItem;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ExchangeViewModel
        extends BaseViewModel<Exchange, ExchangeItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private static final int LIMIT = Constants.Limit.COIN_EXCHANGE;
    private static final long INITIAL_DELAY_IN_SECOND = 0L;
    private static final long PERIOD_IN_SECOND = 10L;
    private static final int RETRY_COUNT = 3;

    private final NetworkManager network;
    private final ExchangeRepository repo;
    private CurrencyFormatter formatter;
    private CmcCurrency cmcCurrency;
    private TimeType timeType;

    @Inject
    ExchangeViewModel(Application application,
                      RxMapper rx,
                      AppExecutors ex,
                      ResponseMapper rm,
                      NetworkManager network,
                      ExchangeRepository repo,
                      CurrencyFormatter formatter) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        this.formatter = formatter;
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
                Response<List<ExchangeItem>> result = getOutputs().getValue();
                if (result == null || result instanceof Response.Failure) {
                    getEx().postToUi(() -> loads(false), 250L);
                }
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void start() {
        network.observe(this, true);
    }

    public void loads(boolean fresh) {
        if (fresh) {
            removeMultipleSubscription();
        }
        if (hasMultipleDisposable()) {
            notifyUiState();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(result -> {
                    postResult(result, true);
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });

        addMultipleSubscription(disposable);

    }

    private Flowable<List<ExchangeItem>> getItemsInterval() {
        return Flowable
                .interval(INITIAL_DELAY_IN_SECOND, PERIOD_IN_SECOND, TimeUnit.SECONDS, getRx().io())
                .map(tick -> getItemsRx().blockingGet())
                .retry(RETRY_COUNT);
    }

    private Maybe<List<ExchangeItem>> getItemsRx() {
        Coin coin = getTask().getInput();
        return repo
                .getItemsRx(coin.getSymbol(), LIMIT)
                .flatMap((Function<List<Exchange>, MaybeSource<List<ExchangeItem>>>) this::getItemsRx);
    }

    private Maybe<List<ExchangeItem>> getItemsRx(List<Exchange> items) {
        return Flowable.fromIterable(items)
                .map(this::getItem)
                .toList()
                .toMaybe();
    }

    private ExchangeItem getItem(Exchange exchange) {
        SmartMap<Long, ExchangeItem> map = getUiMap();
        ExchangeItem item = map.get(exchange.getId());
        if (item == null) {
            item = ExchangeItem.getItem(exchange);
            map.put(exchange.getId(), item);
        }
        item.setItem(exchange);
        return item;
    }
}
