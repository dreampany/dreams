package com.dreampany.word.vm;

import android.app.Application;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.NetworkManager;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class SearchViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private final NetworkManager network;
    private final WordRepository repo;
    private final StateMapper stateMapper;

    @Inject
    SearchViewModel(Application application,
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
        //network.observe(this::onResult, true);
    }

    @DebugLog
    public void loads(String query) {
        if (!preLoads(true)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx(query.toLowerCase()))
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(                        result -> {
                    postProgress(false);
                    postResult(result);
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void toggle(Word word) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(word))
                .subscribe(this::postFlag, this::postFailure);
        addSingleSubscription(disposable);
    }

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            repo.toggleFlag(word);
            return getItem(word);
        });
    }

    @DebugLog
    private Maybe<List<WordItem>> getItemsRx(String query) {
        return repo.getSearchItemsRx(query, Constants.Limit.WORD_SEARCH)
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) this::getItemsRx);
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> words) {
        return Maybe.fromCallable(() -> {
            List<WordItem> items = new ArrayList<>(words.size());
            for (Word word : words) {
                WordItem item = getItem(word);
                items.add(item);
            }
            return items;
        });
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
        List<State> states = repo.getStates(item.getItem());
        Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
        boolean flagged = repo.isFlagged(item.getItem());
        item.setFlagged(flagged);
    }
}
