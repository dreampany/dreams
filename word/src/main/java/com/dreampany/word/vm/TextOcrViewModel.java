package com.dreampany.word.vm;

import android.app.Application;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.model.Store;
import com.dreampany.frame.data.source.repository.StoreRepository;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.enums.ItemType;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/27/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class TextOcrViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private final WordRepository repo;
    private final StoreRepository storeRepo;
    private final StateMapper stateMapper;

    @Inject
    TextOcrViewModel(Application application,
                     RxMapper rx,
                     AppExecutors ex,
                     ResponseMapper rm,
                     WordRepository repo,
                     StoreRepository storeRepo,
                     StateMapper stateMapper) {
        super(application, rx, ex, rm);
        this.repo = repo;
        this.storeRepo = storeRepo;
        this.stateMapper = stateMapper;
    }


    public void loadOcr(String text) {
        if (!takeAction(true, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsOfText(text))
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(                        result -> {
                    postProgress(false);
                   // postResult(result);
                }, error -> {
//                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    private Maybe<List<WordItem>> getItemsOfText(String text) {
        return Maybe.fromCallable(() -> {
            List<String> items = TextUtil.getWords(text);
            List<Word> result = new ArrayList<>();

            //save as OCR text
            long ocrTime = TimeUtil.currentTime();
            Stream.of(items).forEach(item -> {
                Word word = repo.getItemOf(item.toLowerCase(), true);
                if (repo.isExists(word)) {
                    result.add(word);
                    long resultId = putWordOfOcr(word, ocrTime);
                    Timber.v("Result of OCR %s = %d", word.getId(), resultId);
                }
            });
            return getItems(result);
        });
    }


    private List<WordItem> getItems(List<Word> items) {
        return Flowable.fromIterable(items)
                .map(this::getItem)
                .toList()
                .blockingGet();
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

    private long putWordOfOcr(Word word, long timeOfOcr) {
        Store store = new Store(word.getId(), ItemType.WORD.name(), ItemSubtype.OCR.name());
        store.setTime(timeOfOcr);
        store.setData(word.getId());
        return storeRepo.putItem(store);
    }
}
