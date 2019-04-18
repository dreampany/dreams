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
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.model.News;
import com.dreampany.lca.data.source.repository.NewsRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.NewsItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NewsViewModel
        extends BaseViewModel<News, NewsItem, UiTask<News>>
        implements NetworkManager.Callback {

    private final NetworkManager network;
    private final NewsRepository repo;

    private SmartAdapter.Callback<NewsItem> uiCallback;

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
                Response<List<NewsItem>> result = getOutputs().getValue();
                if (result == null || result instanceof Response.Failure) {
                    boolean empty = uiCallback == null || uiCallback.getEmpty();
                    getEx().postToUi(() -> loads(false, empty), 250L);
                }
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void setUiCallback(SmartAdapter.Callback<NewsItem> callback) {
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
                    postResult(Response.Type.GET, result, true);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    /* private api */
    private Maybe<List<NewsItem>> getItemsRx() {
        return repo
                .getItemsRx(Constants.Limit.NEWS)
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
