package com.dreampany.word.vm;

import android.app.Application;

import androidx.annotation.Nullable;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.network.data.model.Network;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import com.dreampany.word.data.source.repository.ApiRepository;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class SearchViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private static final long INITIAL_DELAY = Constants.Time.INSTANCE.getWordPeriod();
    private static final long PERIOD = Constants.Time.INSTANCE.getWordPeriod();
    private static final AtomicBoolean periodically = new AtomicBoolean(false);

    private final NetworkManager network;
    private final Pref pref;
    private final StateMapper stateMapper;
    private final ApiRepository repo;
    private Disposable updateDisposable;
    private SmartAdapter.Callback<WordItem> uiCallback;

    @Inject
    SearchViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network,
                    Pref pref,
                    StateMapper stateMapper,
                    ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.stateMapper = stateMapper;
        this.repo = repo;
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
        this.uiCallback = null;
        removeUpdateDisposable();
        super.clear();
    }

    void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.isConnected()) {
                state = UiState.ONLINE;
                Response<List<WordItem>> result = getOutputs().getValue();
                if (result instanceof Response.Failure) {
                    //getEx().postToUi(() -> loads(false, false), 250L);
                }
                //getEx().postToUi(this::updateItem, 2000L);
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void setUiCallback(SmartAdapter.Callback<WordItem> callback) {
        this.uiCallback = callback;
    }

    public void start() {
        network.observe(this::onResult, true);
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void loadLastSearchWord(boolean progress) {
        if (!takeAction(true, getSingleDisposable())) {
            return;
        }

        Disposable disposable = getRx()
                .backToMain(getLastSearchWordRx())
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.GET, result);
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }

    public void suggests(boolean progress) {
        if (!takeAction(true, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getSuggestionsRx())
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResultOfString(Response.Type.SUGGESTS, result);
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscriptionOfString(disposable);
    }

    public void suggests(String query, boolean progress) {
        if (!takeAction(true, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getSuggestionsRx(query))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.SUGGESTS, result);
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void search(String query, boolean progress) {
        if (!takeAction(true, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx(query.toLowerCase()))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.SEARCH, result);
                    //getEx().postToUi(() -> update(false), 3000L);
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void find(String query, boolean progress) {
        if (!takeAction(true, getSingleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(findItemRx(query))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.SEARCH, result);
                    //getEx().postToUi(() -> update(false), 3000L);
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }

    public void update(boolean progress) {
        Timber.v("update fired");
        if (hasDisposable(updateDisposable)) {
            return;
        }
        removeUpdateDisposable();
        updateDisposable = getRx()
                .backToMain(getVisibleItemIfRx())
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            if (result != null) {
                                if (progress) {
                                    postProgress(false);
                                }
                                //postResult(result);
                                getEx().postToUi(() -> update(progress), 3000L);
                            } else {
                                if (progress) {
                                    postProgress(false);
                                }
                            }
                        }, this::postFailure);
        addSubscription(updateDisposable);
    }

    /**
     * private api
     */
    private Maybe<WordItem> getVisibleItemIfRx() {
        return Maybe.fromCallable(() -> {
            Timber.d("Ticking getVisibleItemIfRxPeriodically");
            WordItem next = getVisibleItemIf();
            if (next == null) {
                return next;
            }
            Timber.d("Success at next to getVisibleItemIf %s", next.getItem().getId());
            return next;
        });
    }

/*    private Flowable<WordItem> getVisibleItemIfRxPeriodically() {
        return Flowable
                .interval(INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS, getRx().io())
                .takeWhile(item -> periodically.get())
                .map(tick -> {
                    Timber.d("Ticking getVisibleItemIfRxPeriodically");
                    WordItem next = getVisibleItemIf();
                    if (next == null) {
                        periodically.set(false);
                        return next;
                    }
                    Timber.d("Success at next to getVisibleItemIf %s", next.getItemRx().getWord());
                    return next;
                });
    }*/

    private WordItem getVisibleItemIf() {
        if (uiCallback == null) {
            return null;
        }
        WordItem next = null;
        List<WordItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            for (WordItem item : items) {
                if (!item.hasState(ItemState.FULL)) {
                    Timber.d("Next Item to getVisibleItemIf %s", item.getItem().getId());
                    Word result = repo.getItemIf(item.getItem());
                    if (result != null) {
                        next = getItem(result, true);
                        Timber.d("Success Result at getVisibleItemIf %s", result.toString());
                        break;
                    } else {
                        Timber.d("Result remains null at getVisibleItemIf");
                    }
                }
            }
        }
        return next;
    }

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            //repo.toggleFlag(word);
            return getItem(word, true);
        });
    }

    private Maybe<List<String>> getSuggestionsRx() {
        return repo.getAllRawWordsRx();
    }

    private Maybe<WordItem> getLastSearchWordRx() {
        return Maybe.create(emitter -> {
            Word word = pref.getLastSearchWord();
            WordItem result = getItem(word, true);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    private Maybe<List<WordItem>> getSuggestionsRx(String query) {
        return getSuggestionsRx(query, Constants.Limit.WORD_SUGGESTION)
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) words -> getItemsRx(words, false));
    }

    private Maybe<WordItem> findItemRx(String query) {
        return Maybe.create(emitter -> {
            Word word = repo.getItem(query, false);
            Word fullWord = repo.getItemIf(word);
            WordItem result = null;
            if (fullWord != null) {
                pref.setLastSearchWord(fullWord);
                result = getItem(fullWord, true);
            }

            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    private Maybe<List<WordItem>> getItemsRx(String query) {
        return getSearchItemsRx(query, Constants.Limit.WORD_SEARCH)
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) words -> getItemsRx(words, true));
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> words, boolean fully) {
        return Flowable.fromIterable(words)
                .map(word -> getItem(word, fully))
                .toList()
                .toMaybe();
    }

    private WordItem getItem(Word word, boolean fully) {
        SmartMap<String, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.getSimpleItem(word);
            map.put(word.getId(), item);
        }
        item.setItem(word);
        if (fully) {
            adjustState(item);
        }
        return item;
    }

    private void adjustState(WordItem item) {
        List<State> states = repo.getStates(item.getItem());
        Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState())));
    }

    private List<Word> getSearchItems(String query, int limit) {
        Word word = repo.getItem(query, false);
        List<Word> result = new ArrayList<>();
        if (word != null) {
            result.add(word);
        } else {
            List<Word> items = repo.getSearchItems(query, limit);
            if (!DataUtil.isEmpty(items)) {
                result.addAll(items);
            }
        }
        return result;
    }

    private Maybe<List<Word>> getSearchItemsRx(String query, int limit) {
        return Maybe.create(emitter -> {
            List<Word> result = getSearchItems(query, limit);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    private List<Word> getSuggestions(String query, int limit) {
        List<Word> result = repo.getSearchItems(query, limit);
        return result;
    }

    private Maybe<List<Word>> getSuggestionsRx(String query, int limit) {
        return Maybe.create(emitter -> {
            List<Word> result = getSuggestions(query, limit);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }
}
