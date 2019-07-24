package com.dreampany.word.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class RecentViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private static final boolean OPEN = true;

    private static final long INITIAL_DELAY = Constants.Time.INSTANCE.getWordPeriod();
    private static final long PERIOD = Constants.Time.INSTANCE.getWordPeriod();
    private static final int RETRY = 2;

    private final NetworkManager network;
    private final WordRepository repo;
    private final StateMapper stateMapper;

    private Disposable updateDisposable;

    private SmartAdapter.Callback<WordItem> uiCallback;

    @Inject
    RecentViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network,
                    WordRepository repo,
                    StateMapper stateMapper) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        this.stateMapper = stateMapper;
    }

    @Override
    public void clear() {
        //network.deObserve(this::onResult, true);
        this.uiCallback = null;
        removeUpdateDisposable();
        super.clear();
    }


    public void setUiCallback(SmartAdapter.Callback<WordItem> callback) {
        this.uiCallback = callback;
    }

    public void start() {
        //network.observe(this::onResult, true);
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
        if (!takeAction(fresh, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getRecentItemsRx())
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            postProgress(false);
                            //postResult(result);
                            update(false);
                        },
                        error ->
                        {//postFailureMultiple(new MultiException(error, new ExtraException()));
                        }
                );
        addMultipleSubscription(disposable);
    }

    public void update(boolean withProgress) {
        if (!OPEN) {
            return;
        }
        Timber.v("update fired");
        if (hasDisposable(updateDisposable)) {
            return;
        }
        updateDisposable = getRx()
                .backToMain(getVisibleItemIfRx())
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            postProgress(false);
                            //postResult(result);
                        }, this::postFailure);
        addSubscription(updateDisposable);
    }

    /**
     * private api
     */
    private Maybe<List<WordItem>> getRecentItemsRx() {
        return Maybe.empty();
/*        return repo.getRecentItemsRx(Constants.Limit.WORD_RECENT)
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) this::getItemsRx);*/
    }

/*    private Maybe<List<WordItem>> getVisibleItemsIfRx() {
        return Maybe.fromCallable(() -> {
            List<WordItem> result = getVisibleItemsIf();
            if (DataUtil.isEmpty(result)) {
                throw new EmptyException();
            }
            return result;
        }).onErrorResumeNext(Maybe.empty());
    }*/

    private Flowable<WordItem> getVisibleItemIfRx() {
        return Flowable
                .interval(INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> getVisibleItemIf());
    }

    private WordItem getVisibleItemIf() {
        if (uiCallback == null) {
            return null;
        }
        List<WordItem> items = uiCallback.getVisibleItems();
        if (DataUtil.isEmpty(items)) {
            return null;
        }
        for (WordItem item : items) {
/*            if (!repo.hasState(item.getItemRx(), ItemState.STATE, ItemSubstate.FULL)) {
                return repo.getItemRx(item.getItemRx().getWord()).map(this::getItemRx).blockingGet();
            }*/
        }
        return null;
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> items) {
        return Flowable.fromIterable(items)
                .map(this::getItem)
                .toList()
                .toMaybe();
    }

/*    private List<WordItem> getVisibleItemsIf() {
        if (uiCallback == null) {
            return null;
        }
        List<WordItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            List<Word> words = new ArrayList<>();
            for (WordItem item : items) {
                words.add(item.getItemRx());
            }
            items = null;
            if (!DataUtil.isEmpty(words)) {
                List<Word> result = repo.getItems();
                if (!DataUtil.isEmpty(result)) {
                    items = getItemsRx(result).blockingGet();
                }
            }
        }
        return items;
    }*/

    private WordItem getItem(Word word) {
        SmartMap<String, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.Companion.getItem(word);
            map.put(word.getId(), item);
        }
        item.setItem(word);
        adjustState(item);
        adjustFlag(item);
        return item;
    }

    private void adjustState(WordItem item) {
        //List<State> states = repo.getStates(item.getItemRx());
        //Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
        //boolean flagged = repo.isFavorite(item.getItemRx());
        //item.setFavorite(flagged);
    }

    /*
    public void search(boolean fresh) {
        if (!preLoads(fresh)) {
            updateVisibleItems();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getRecentItemsRx(fresh))
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(result -> {
                    postProgress(false);
                    postResult(result);
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
        updateVisibleItems();
    }

    public void updateVisibleItemIf() {
        if (hasDisposable(updateDisposable)) {
            Timber.v("Updater Running...");
            return;
        }
        updateDisposable = getRx()
                .backToMain(updateItemInterval())
                .subscribe(result -> {
                    postProgress(false);
                    postResult(result);
                }, this::postFailure);
        addSubscription(updateDisposable);
    }

    public void load(String word) {
        if (!preLoad(true)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemRx(word))
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(
                        result -> {
                            postProgress(false);
                            postResult(result);
                        },
                        error -> postFailureMultiple(new MultiException(error, new ExtraException()))
                );
        addSingleSubscription(disposable);
    }

    public void toggle(Word word) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(word))
                .subscribe(this::postFavorite, this::postFailure);
        addSingleSubscription(disposable);
    }

    public void updateVisibleItems() {
        if (hasDisposable(updateVisibleItemsDisposable)) {
            return;
        }
        updateVisibleItemsDisposable = getRx()
                .backToMain(getVisibleItemsRx())
                .subscribe(this::postResult, error -> {

                });
        addSubscription(updateVisibleItemsDisposable);
    }

    private Maybe<List<WordItem>> getVisibleItemsRx() {
        return Maybe.fromCallable(() -> {
            List<WordItem> items = uiCallback.getVisibleItems();
            if (!DataUtil.isEmpty(items)) {
                for (WordItem item : items) {
                    item.setItem(repo.getItemRx(item.getItemRx().getWord()));
                    adjustState(item);
                    adjustFlag(item);
                }
            }
            return items;
        });
    }

    private Maybe<List<WordItem>> getRecentItemsRx(boolean fresh) {
        return repo.getRecentItemsRx(Constants.Limit.WORD_RECENT)
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) words -> getItemsRx(words, fresh));
    }

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            repo.toggleFlag(word);
            return getItemRx(word);
        });
    }

    private Maybe<WordItem> getItemRx(String word) {
        return repo.getItemRx(word).map(this::getItemRx);
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> items, boolean fresh) {
        return Flowable.fromIterable(items)
                .map(this::getItemRx)
                .toList()
                .toMaybe();
    }

    private Flowable<WordItem> updateItemInterval() {
        Flowable<WordItem> flowable = Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> {
                    WordItem next = null;
                    if (uiCallback != null) {
                        List<WordItem> items = uiCallback.getVisibleItems();
                        if (!DataUtil.isEmpty(items)) {
                            for (WordItem item : items) {
                                if (!repo.hasState(item.getItemRx(), ItemState.STATE, ItemSubstate.FULL)) {
                                    Timber.d("Next Item to updateVisibleItemIf %s", item.getItemRx().getWord());
                                    getEx().postToUi(() -> postProgress(true));
                                    next = updateItemRx(item.getItemRx()).blockingGet();
                                    break;
                                }
                            }
                        }
                    }
                    return next;
                });
        return flowable;
    }

    private Maybe<WordItem> updateItemRx(Word item) {
        return repo.getItemRx(item.getWord()).map(this::getItemRx);
    }

    private WordItem getItemRx(Word word) {
        SmartMap<Long, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.getSimpleItem(word);
            map.put(word.getId(), item);
        }
        item.setItem(word);
        adjustState(item);
        adjustFlag(item);
        return item;
    }

    private void adjustState(WordItem item) {
        List<State> states = repo.getStates(item.getItemRx());
        Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
        boolean flagged = repo.isFavorite(item.getItemRx());
        item.setFavorite(flagged);
    }*/
}