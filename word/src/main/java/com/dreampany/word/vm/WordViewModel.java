/*
package com.dreampany.word.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import com.dreampany.word.data.source.repository.ApiRepository;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;
import com.dreampany.word.util.Util;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

*/
/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

public class WordViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> implements NetworkManager.Callback {

    private final NetworkManager network;
    private final Pref pref;
    private final WordMapper mapper;
    private final StateMapper stateMapper;
    private final ApiRepository repo;

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
        network.deObserve(this);
        super.clear();
    }

    @Override
    public void onNetworkResult(@NonNull List<Network> networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.getConnected()) {
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
        network.observe(this, true);
    }


    public void refresh(Word word, boolean onlyUpdate, boolean withProgress) {
        if (onlyUpdate) {
            //update(withProgress);
            return;
        }
        load(word, true, withProgress);
    }

    public void load(Word word, boolean fresh, boolean withProgress) {
        if (!takeAction(fresh, getMultipleDisposable())) {
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
                    postResult(Response.Type.GET, result);
                }, error -> {
                    postFailure(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }

    public void toggleFavorite(Word word) {
        Disposable disposable = getRx()
                .backToMain(toggleImpl(word))
                .subscribe(result -> postResult(Response.Type.UPDATE, result, false), this::postFailure);
    }

    private Maybe<WordItem> getItemRx(Word word) {
        return repo.getItemIfRx(word).map(this::getItem);
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
*/
/*        if (fully) {
            adjustState(item);
        }*//*

        return item;
    }

    private void adjustState(WordItem item) {
        List<State> states = repo.getStates(item.getItem());
        Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState())));
    }

    private void adjustFavorite(Word word, WordItem item) {
        item.setFavorite(repo.isFavorite(word));
    }

    private Maybe<WordItem> toggleImpl(Word word) {
        return Maybe.fromCallable(() -> {
            repo.toggleFavorite(word);
            return getItem(word);
        });
    }

    public void share(Fragment fragment) {
        Word word = getTask().getInput();
        String subject = word.getId();
        String text = Util.getText(word);
        AndroidUtil.share(fragment, subject, text);
    }
}
*/
