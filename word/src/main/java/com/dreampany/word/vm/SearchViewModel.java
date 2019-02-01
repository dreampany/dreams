package com.dreampany.word.vm;

import android.app.Application;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.NetworkManager;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.ApiRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class SearchViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private final NetworkManager network;
    private final ApiRepository repo;;

    @Inject
    SearchViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    NetworkManager network,
                    ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;;
    }

    public void suggests(String query) {
        if (!preLoads(true)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getSuggestionsRx(query.toLowerCase()))
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
    }

    public void search(String query) {
        if (!preLoads(true)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx(query.toLowerCase()))
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

    /** private api */
    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            //repo.toggleFlag(word);
            return getItem(word, true);
        });
    }

    private Maybe<List<WordItem>> getSuggestionsRx(String query) {
        return getSuggestionsRx(query, Constants.Limit.WORD_SEARCH)
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) words -> getItemsRx(words, false));
    }

    private Maybe<List<WordItem>> getItemsRx(String query) {
        return repo.getSearchItemsRx(query, Constants.Limit.WORD_SEARCH)
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) words -> getItemsRx(words, true));
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> words, boolean fully) {
        return Flowable.fromIterable(words)
                .map(word -> getItem(word, fully))
                .toList()
                .toMaybe();
    }

    private WordItem getItem(Word word, boolean fully) {
        SmartMap<Long, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.getSimpleItem(word);
            map.put(word.getId(), item);
        }
        item.setItem(word);
        if (fully) {
            adjustState(item);
            adjustFlag(item);
        }
        return item;
    }

    private void adjustState(WordItem item) {
       // List<State> states = repo.getStates(item.getItem());
      //  Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
/*        boolean flagged = repo.isFlagged(item.getItem());
        item.setFlagged(flagged);*/
    }

    private Maybe<List<Word>> getSuggestionsRx(String query, int limit) {
        return Maybe.empty();
    }
}
