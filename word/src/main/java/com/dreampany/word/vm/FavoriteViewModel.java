package com.dreampany.word.vm;

import android.app.Application;

import com.dreampany.frame.data.model.Response;
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
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import com.dreampany.word.data.source.repository.ApiRepository;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class FavoriteViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private final NetworkManager network;
    private final Pref pref;
    private final ApiRepository repo;
    private SmartAdapter.Callback<WordItem> uiCallback;
    private Disposable updateDisposable;

    @Inject
    FavoriteViewModel(Application application,
                      RxMapper rx,
                      AppExecutors ex,
                      ResponseMapper rm,
                      NetworkManager network,
                      Pref pref,
                      ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
    }

    @Override
    public void clear() {
        //network.deObserve(this, true);
        this.uiCallback = null;
        super.clear();
    }

    public void setUiCallback(SmartAdapter.Callback<WordItem> callback) {
        this.uiCallback = callback;
    }

    public void refresh(boolean update, boolean important, boolean progress) {
        if (update) {
            update(important, progress);
            return;
        }
        loads(important, progress);
    }

    public void loads(boolean important, boolean progress) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getFavoriteItemsRx())
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.ADD, result);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void update(boolean important, boolean progress) {
        if (!takeAction(important, getSingleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getVisibleItemsIfRx())
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            if (progress) {
                                postProgress(false);
                            }
                            postResult(Response.Type.UPDATE, result);
                        }, error -> {
                            if (progress) {
                                postProgress(false);
                            }
                            postFailures(new MultiException(error, new ExtraException()));
                        });
        addMultipleSubscription(disposable);
    }

    public void toggleFavorite(Word word) {
        Disposable disposable = getRx()
                .backToMain(toggleImpl(word))
                .subscribe(result -> postResult(Response.Type.UPDATE, result, false), this::postFailure);
    }

    /* private api */
    private List<WordItem> getVisibleItemsIf() {
        if (uiCallback == null) {
            return null;
        }
        List<WordItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            List<String> wordIds = new ArrayList<>();
            for (WordItem item : items) {
                wordIds.add(item.getItem().getId());
            }
            items = null;
            if (!DataUtil.isEmpty(wordIds)) {
                List<Word> words = repo.getItemsIf(wordIds);
                if (!DataUtil.isEmpty(words)) {
                    items = getItems(words);
                }
            }
        }
        return items;
    }


    private Maybe<List<WordItem>> getVisibleItemsIfRx() {
        return Maybe.fromCallable(() -> {
            List<WordItem> result = getVisibleItemsIf();
            if (DataUtil.isEmpty(result)) {
                throw new EmptyException();
            }
            return result;
        }).onErrorReturn(throwable -> new ArrayList<>());
    }


    private List<WordItem> getFavoriteItems() {
        List<WordItem> result = new ArrayList<>();
        List<Word> real = repo.getFavorites();
        if (real == null) {
            real = new ArrayList<>();
        }
        List<WordItem> ui = uiCallback.getItems();
        for (Word word : real) {
            WordItem item = getItem(word);
            item.setFavorite(true);
            result.add(item);
        }

        if (!DataUtil.isEmpty(ui)) {
            for (WordItem item : ui) {
                if (!real.contains(item.getItem())) {
                    item.setFavorite(false);
                    result.add(item);
                }
            }
        }
        return result;
    }

    private Maybe<List<WordItem>> getFavoriteItemsRx() {
        return Maybe.create(emitter -> {
            List<WordItem> result = getFavoriteItems();
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

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            repo.toggleFavorite(word);
            return getItem(word);
        });
    }

    private void adjustFavorite(Word word, WordItem item) {
        item.setFavorite(repo.isFavorite(word));
    }

    private WordItem getItem(Word word) {
        SmartMap<String, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.getSimpleItem(word);
            map.put(word.getId(), item);
        }
        item.setItem(word);
        adjustFavorite(word, item);
        return item;
    }

    private List<WordItem> getItems(List<Word> result) {
        List<WordItem> items = new ArrayList<>(result.size());
        for (Word word : result) {
            WordItem item = getItem(word);
            items.add(item);
        }
        return items;
    }


/*    @DebugLog
    public void loads(boolean fresh) {
        if (!takeAction(fresh, getMultipleDisposable())) {
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
 *//*       updateVisibleItemsDisposable = getRx()
                .backToMain(getVisibleItemsRx())
                .subscribe(this::postResult, error -> {

                });
        addSubscription(updateVisibleItemsDisposable);*//*
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
                    item.setItem(repo.getItem(item.getItem().getId(), true));
                    adjustState(item);
                    adjustFlag(item);
                }
            }
            return items;
        });
    }

    private Maybe<List<WordItem>> getItemsRx() {

        return Maybe.empty();
        *//*        return repo.getFlagsRx()
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) this::getItemsRx);*//*
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
  *//*                              if (!repo.hasState(item.getItemRx(), ItemState.STATE, ItemSubstate.FULL)) {
                                    Timber.d("Next Item to updateVisibleItemIf %s", item.getItemRx().getWord());
                                    getEx().postToUi(() -> postProgress(true));
                                    next = updateItemRx(item.getItemRx()).blockingGet();
                                    break;
                                }*//*
                            }
                        }
                    }
                    return next;
                });
        return flowable;
    }

    private Maybe<WordItem> updateItemRx(Word item) {
        return repo.getItemRx(item.getId(), true).map(this::getItem);
    }

    private WordItem getItem(Word word) {
        SmartMap<String, WordItem> map = getUiMap();
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
        //boolean flagged = repo.isFavorite(item.getItemRx());
        //item.setFavorite(flagged);
    }*/
}
