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
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 11/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class TodayViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private final NetworkManager network;
    private final WordRepository repo;
    private final StateMapper stateMapper;

    @Inject
    TodayViewModel(Application application,
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

    public void loadToday() {
        if (!takeAction(true, getSingleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getTodayItemRx())
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(
                        result -> {
                            postProgress(false);
                            //postResult(result);
                        },
                        error ->
                        {//postFailureMultiple(new MultiException(error, new ExtraException()));
                        }
                );
        addSingleSubscription(disposable);
    }

    private Maybe<WordItem> getTodayItemRx() {
        return repo.getTodayItemRx().map(this::getItem);
    }

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
}