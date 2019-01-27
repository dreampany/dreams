package com.dreampany.word.vm;

import android.app.Application;
import androidx.fragment.app.Fragment;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.NetworkManager;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.util.Util;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class WordViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private final NetworkManager network;
    private final WordRepository repo;
    private final StateMapper stateMapper;

    @Inject
    WordViewModel(Application application,
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

    @Override
    public void clear() {
        //network.deObserve(this::onResult, true);
        super.clear();
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

    public void toggle() {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(getTask().getInput()))
                .subscribe(this::postFlag, this::postFailure);
        addSingleSubscription(disposable);
    }

    private Maybe<WordItem> getItemRx(String word) {
        return repo.getItemRx(word).map(this::getItem);
    }

    private WordItem getItem(Word word) {
        SmartMap<Long, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.getSimpleItem(word);
            map.put(word.getId(), item);
        }
        getTask().setInput(word);
        item.setItem(word);
        adjustState(item);
        adjustFlag(item);
        //put as Recent
        repo.putState(word, ItemState.RECENT);
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

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            repo.toggleFlag(word);
            return getItem(word);
        });
    }

    public void share(Fragment fragment) {
        Word word = getTask().getInput();
        String subject = word.getWord();
        String text = Util.getText(word);
        AndroidUtil.share(fragment, subject, text);
    }
}
