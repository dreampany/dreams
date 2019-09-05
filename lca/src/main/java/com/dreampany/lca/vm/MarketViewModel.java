package com.dreampany.lca.vm;

import android.app.Application;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.dreampany.framework.data.enums.UiState;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.misc.exception.ExtraException;
import com.dreampany.framework.misc.exception.MultiException;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.vm.BaseViewModel;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Market;
import com.dreampany.lca.data.source.repository.MarketRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.misc.CurrencyFormatter;
import com.dreampany.lca.ui.activity.ToolsActivity;
import com.dreampany.lca.ui.enums.UiSubtype;
import com.dreampany.lca.ui.enums.UiType;
import com.dreampany.lca.ui.model.MarketItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.manager.NetworkManager;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MarketViewModel
        extends BaseViewModel<Market, MarketItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private static final int LIMIT = Constants.Limit.COIN_MARKET;

    private final NetworkManager network;
    private final MarketRepository repo;
    private CurrencyFormatter formatter;
    private String toSymbol;

    @Inject
    MarketViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network,
                    MarketRepository repo,
                    CurrencyFormatter formatter) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        this.formatter = formatter;
    }

    @Override
    public void clear() {
        network.deObserve(this);
        super.clear();
    }

    @Override
    public void onNetworkResult(@NonNull List<Network> networks) {

        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.getInternet()) {
                state = UiState.ONLINE;
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void start() {
        network.observe(this, true);
    }

    public void loads(String toSymbol, boolean important) {
        this.toSymbol = toSymbol;
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(result -> postResult(Response.Type.ADD,result, true), error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    private Maybe<List<MarketItem>> getItemsRx() {
        Coin coin = getTask().getInput();
        return repo
                .getItemsRx(coin.getSymbol(), toSymbol, LIMIT)
                .flatMap((Function<List<Market>, MaybeSource<List<MarketItem>>>) this::getItemsRx);
    }

    private Maybe<List<MarketItem>> getItemsRx(List<Market> items) {
        return Flowable.fromIterable(items)
                .map(this::getItem)
                .toList()
                .toMaybe();
    }

    private MarketItem getItem(Market market) {
        SmartMap<String, MarketItem> map = getUiMap();
        MarketItem item = map.get(market.getId());
        if (item == null) {
            item = MarketItem.getItem(market);
            map.put(market.getId(), item);
        }
        item.setItem(market);
        adjust(item);
        return item;
    }

    private void adjust(MarketItem item) {
        Market market = item.getItem();
        item.setVolume24h(formatter.format(market.getToSymbol(), market.getVolume24h()));
        item.setChangePct24hFormat(getChangePc24hFormat(market.getChangePct24h()));
        item.setChangePct24hColor(getChangePc24hColor(market.getChangePct24h()));
        item.setPrice(formatter.format(market.getToSymbol(), market.getPrice()));
    }

    @StringRes
    private int getChangePc24hFormat(double changePct24h) {
        return changePct24h >= 0.0f ? R.string.positive_pct_format : R.string.negative_pct_format;
    }

    @ColorRes
    private int getChangePc24hColor(double changePct24h) {
        return changePct24h >= 0.0f ? R.color.material_green500 : R.color.material_red500;
    }

    public void openMarket(BaseFragment fragment, String market) {
        String webUrl = Constants.Api.CryptoCompareMarketOverviewUrl;
        String url = String.format(webUrl, market);
        UiTask<?> task = new UiTask<Market>(true, UiType.SITE, UiSubtype.VIEW, null, url);
        fragment.openActivity(ToolsActivity.class, task);
    }
}
