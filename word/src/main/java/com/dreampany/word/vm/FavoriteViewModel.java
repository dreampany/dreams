package com.dreampany.word.vm;

import android.app.Application;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.repository.StateRepository;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.Favorite;
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
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.enums.ItemType;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import com.dreampany.word.data.source.repository.WordRepository;
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
    private final WordMapper mapper;
    private final StateRepository stateRepo;
    private final WordRepository wordRepo;
    private final SmartMap<String, Boolean> favorites;
    private SmartAdapter.Callback<WordItem> uiCallback;

    @Inject
    FavoriteViewModel(Application application,
                      RxMapper rx,
                      AppExecutors ex,
                      ResponseMapper rm,
                      NetworkManager network,
                      Pref pref,
                      WordMapper mapper,
                      StateRepository stateRepo,
                      WordRepository wordRepo,
                      @Favorite SmartMap<String, Boolean> favorites) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.mapper = mapper;
        this.stateRepo = stateRepo;
        this.wordRepo = wordRepo;
        this.favorites = favorites;
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
                List<Word> words = wordRepo.getItemsRx(wordIds).blockingGet();
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
        List<Word> real = getFavorites();
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
            toggleFavorite(word);
            return getItem(word);
        });
    }

    private void adjustFavorite(Word word, WordItem item) {
        item.setFavorite(isFavorite(word));
    }

    private WordItem getItem(Word word) {
        SmartMap<String, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.Companion.getItem(word);
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

    public List<Word> getFavorites() {
        List<State> states = stateRepo.getItems(ItemType.WORD.name(), ItemSubtype.DEFAULT.name(), ItemState.FAVORITE.name());
        return getItemsOfStatesIf(states);
    }

    private List<Word> getItemsOfStatesIf(List<State> states) {
        if (DataUtil.isEmpty(states)) {
            return null;
        }
        List<Word> result = new ArrayList<>(states.size());
        Stream.of(states).forEach(state -> {
            Word item = mapper.toItem(state, wordRepo);
            if (item != null) {
                result.add(item);
            }
        });
        return result;
    }

    private boolean isFavorite(Word word) {
        if (!favorites.contains(word.getId())) {
            boolean favorite = hasState(word, ItemSubtype.DEFAULT, ItemState.FAVORITE);
            favorites.put(word.getId(), favorite);
        }
        return favorites.get(word.getId());
    }

    private boolean hasState(Word word, ItemSubtype subtype,  ItemState state) {
        return stateRepo.getCountById(word.getId(), ItemType.WORD.name(), subtype.name(), state.name()) > 0;
    }
}
