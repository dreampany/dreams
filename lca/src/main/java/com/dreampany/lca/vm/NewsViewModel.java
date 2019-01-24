package com.dreampany.lca.vm;

import android.app.Application;
import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.model.News;
import com.dreampany.lca.data.source.repository.NewsRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.NewsItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.NetworkManager;
import com.dreampany.network.data.model.Network;
import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NewsViewModel extends BaseViewModel<News, NewsItem, UiTask<News>> {

    private static final int LIMIT = Constants.Limit.NEWS;

    private final NetworkManager network;
    private final NewsRepository repo;

    @Inject
    NewsViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  NewsRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        repo.clear();
        super.clear();
    }

    @DebugLog
    void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.isConnected()) {
                state = UiState.ONLINE;
                Response<List<NewsItem>> result = getOutputs().getValue();
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
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(result -> postResult(result, true), error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

/*    private Flowable<List<NewsItem>> getItemsInterval() {
        return Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> getItemsRx().blockingGet())
                .retry(RETRY_COUNT);
    }*/

    private Maybe<List<NewsItem>> getItemsRx() {
        return repo
                .getItemsRx(LIMIT, true)
                .flatMap((Function<List<News>, MaybeSource<List<NewsItem>>>) this::getItemsRx);
    }

    private Maybe<List<NewsItem>> getItemsRx(List<News> items) {
        return Maybe.fromCallable(() -> {
            //remove already in UI
            List<News> filtered = new ArrayList<>();
            SmartMap<Long, NewsItem> ui = getUiMap();
            Stream.of(items).forEach(news -> {
                if (!ui.contains(news.getId())) {
                    filtered.add(news);
                }
            });

            List<NewsItem> result = new ArrayList<>(filtered.size());
            for (News news : filtered) {
                NewsItem item = getItem(news);
                result.add(item);
            }
            return result;
        });
    }

    private NewsItem getItem(News news) {
        SmartMap<Long, NewsItem> map = getUiMap();
        NewsItem item = map.get(news.getId());
        if (item == null) {
            item = NewsItem.getItem(news);
            map.put(news.getId(), item);
        }
        item.setItem(news);
        return item;
    }
}
