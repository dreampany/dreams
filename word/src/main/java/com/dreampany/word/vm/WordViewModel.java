package com.dreampany.word.vm;

import android.app.Application;
import androidx.fragment.app.Fragment;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.network.data.model.Network;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import com.dreampany.word.data.source.repository.ApiRepository;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.util.Util;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class WordViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private final NetworkManager network;
    private final Pref pref;
    private final WordMapper mapper;
    private final StateMapper stateMapper;
    private final ApiRepository repo;
    private Disposable updateDisposable;

    @Inject
    WordViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  Pref pref,
                  WordMapper mapper,
                  StateMapper stateMapper,
                  ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.mapper = mapper;
        this.stateMapper = stateMapper;
        this.repo = repo;
    }

    @Override
    public void clear() {
        network.deObserve(this::onResult, true);
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

    public void start() {
        network.observe(this::onResult, true);
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void refresh(Word word, boolean onlyUpdate, boolean withProgress) {
        if (onlyUpdate) {
            //update(withProgress);
            return;
        }
        load(word, true, withProgress);
    }

    public void load(Word word, boolean fresh, boolean withProgress) {
        if (!preLoad(true)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemRx(word))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                   // postResult(result);
                }, error -> {
                    //postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }

    public void load(String word, boolean fresh, boolean withProgress) {

    }

    public void toggle() {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(getTask().getInput()))
                .subscribe(this::postFavorite, this::postFailure);
        addSingleSubscription(disposable);
    }

    public Word toWord(String word) {
        return mapper.toItem(word.toLowerCase());
    }

    private Maybe<WordItem> getItemRx(Word word) {
        return repo.getItemIfRx(word).map(this::getItem);
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
        //repo.putState(word, ItemState.RECENT);
        return item;
    }

    private void adjustState(WordItem item) {
        //List<State> states = repo.getStates(item.getItemRx());
        //Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
        //boolean flagged = repo.isFlagged(item.getItemRx());
        //item.setFlagged(flagged);
    }

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            //repo.toggleFlag(word);
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
