package com.dreampany.word.vm;

import android.app.Application;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
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
import hugo.weaving.DebugLog;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class FavoriteViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private static final long initialDelay = Constants.Time.INSTANCE.getWordPeriod();
    private static final long period = Constants.Time.INSTANCE.getWordPeriod();

    private final NetworkManager network;
    private final WordRepository repo;
    private final StateMapper stateMapper;
    private SmartAdapter.Callback<WordItem> uiCallback;
    private Disposable updateDisposable, updateVisibleItemsDisposable;

    @Inject
    FavoriteViewModel(Application application,
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
        removeSubscription(updateDisposable);
        removeSubscription(updateVisibleItemsDisposable);
        super.clear();
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void removeUpdateVisibleItemsDisposable() {
        removeSubscription(updateVisibleItemsDisposable);
    }

    public void setUiCallback(SmartAdapter.Callback<WordItem> callback) {
        this.uiCallback = callback;
    }

    @DebugLog
    public void loads(boolean fresh) {
        if (!preLoads(fresh)) {
            updateVisibleItems();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(result -> {
                    postProgress(false);
                    //postResult(result);
                }, error -> {
                    //postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
        updateVisibleItems();
    }

    public void update() {
        if (hasDisposable(updateDisposable)) {
            Timber.v("Updater Running...");
            return;
        }
        updateDisposable = getRx()
                .backToMain(updateItemInterval())
                .subscribe(result -> {
                    postProgress(false);
                    //postResult(result);
                }, this::postFailure);
        addSubscription(updateDisposable);
    }

    public void updateVisibleItems() {
        if (hasDisposable(updateVisibleItemsDisposable)) {
            return;
        }
 /*       updateVisibleItemsDisposable = getRx()
                .backToMain(getVisibleItemsRx())
                .subscribe(this::postResult, error -> {

                });
        addSubscription(updateVisibleItemsDisposable);*/
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

    private Maybe<List<WordItem>> getVisibleItemsRx() {
        return Maybe.fromCallable(() -> {
            List<WordItem> items = uiCallback.getVisibleItems();
            if (!DataUtil.isEmpty(items)) {
                for (WordItem item : items) {
                    item.setItem(repo.getItem(item.getItem().getWord(), true));
                    adjustState(item);
                    adjustFlag(item);
                }
            }
            return items;
        });
    }

    private Maybe<List<WordItem>> getItemsRx() {

        return Maybe.empty();
        /*        return repo.getFlagsRx()
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) this::getItemsRx);*/
    }

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            //repo.toggleFlag(word);
            return getItem(word);
        });
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> items) {
        return Flowable.fromIterable(items)
                .map(this::getItem)
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
  /*                              if (!repo.hasState(item.getItemRx(), ItemState.STATE, ItemSubstate.FULL)) {
                                    Timber.d("Next Item to updateVisibleItemIf %s", item.getItemRx().getWord());
                                    getEx().postToUi(() -> postProgress(true));
                                    next = updateItemRx(item.getItemRx()).blockingGet();
                                    break;
                                }*/
                            }
                        }
                    }
                    return next;
                });
        return flowable;
    }

    private Maybe<WordItem> updateItemRx(Word item) {
        return repo.getItemRx(item.getWord(), true).map(this::getItem);
    }

    private WordItem getItem(Word word) {
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
        //List<State> states = repo.getStates(item.getItemRx(), ItemState.STATE);
       // Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
        //boolean flagged = repo.isFlagged(item.getItemRx());
        //item.setFlagged(flagged);
    }
}
