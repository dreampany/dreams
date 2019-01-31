package com.dreampany.word.vm;

import android.app.Application;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.word.R;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubstate;
import com.dreampany.word.data.model.Load;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.LoadPref;
import com.dreampany.word.data.source.repository.ApiRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.LoadItem;
import com.dreampany.word.ui.model.UiTask;
import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class LoaderViewModel extends BaseViewModel<Load, LoadItem, UiTask<Load>> {

    private final LoadPref pref;
    private final ApiRepository repo;
    private final List<Word> commonWords;
    private final List<Word> alphaWords;
    private final int totalResId;

    @Inject
    LoaderViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    LoadPref pref,
                    ApiRepository repo) {
        super(application, rx, ex, rm);
        this.pref = pref;
        this.repo = repo;
        commonWords = Collections.synchronizedList(new ArrayList<>());
        alphaWords = Collections.synchronizedList(new ArrayList<>());

        totalResId = R.string.total_words;
    }

    @NotNull
    @Override
    protected Maybe<String> getSubtitle() {
        return Maybe.fromCallable(() -> {
            int total = getMaxStateCount();
            String title = TextUtil.getString(getApplication(), totalResId, total);
            return title;
        });
    }

    public void loads() {
        boolean commonLoaded = pref.isCommonLoaded();
        if (!commonLoaded) {
            loadCommons();
            return;
        }
        if (!pref.isAlphaLoaded()) {
            loadAlphas();
        }
    }

    public void loadCommons() {
        if (!preLoads(false)) {
            return;
        }
        Timber.v("loadCommons running...");
        Disposable disposable = getRx()
                .backToMain(getCommonItemsRxV2())
                .subscribe(result -> {
                    postResult(result);
                    loadAlphas();
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    @DebugLog
    public void loadAlphas() {
        if (!preLoads(false)) {
            Timber.v("loadAlphas already running...");
            return;
        }
        Timber.v("loadAlphas running...");
        Disposable disposable = getRx()
                .backToMain(getAlphaItemsRxV2())
                .subscribe(
                        this::postResult,
                        error -> {
                            postFailureMultiple(new MultiException(error, new ExtraException()));
                        });
        addMultipleSubscription(disposable);
    }

    public void pause() {
        preLoads(true);
    }

    @DebugLog
    private Maybe<LoadItem> getCommonItemsRxV2() {
        return Maybe.fromCallable(() -> {
            LoadItem item = LoadItem.getSimpleItem();
            if (pref.isCommonLoaded()) {
                int current = getMaxStateCount();
                Load load = new Load(current, current);
                item.setItem(load);
                return item;
            }

            AppExecutors ex = getEx();
            if (commonWords.size() != Constants.Count.WORD_COMMON) {
                List<Word> words = repo.getCommonWords();
                commonWords.clear();
                commonWords.addAll(words);
            }
            Word last = pref.getLastWord();
            int lastIndex = last != null ? commonWords.indexOf(last) : -1;
            int current = getMaxStateCount();
            Load load = new Load(current, current);
            item.setItem(load);
            ex.postToUi(() -> postResult(item));
            Word word;
            if (lastIndex != 0) {
                DataUtil.removeFirst(commonWords, lastIndex + 1);
            }

            while (!commonWords.isEmpty()) {
                word = commonWords.remove(0);
                long result = repo.putItem(word, ItemState.STATE, ItemSubstate.RAW);
                if (result != -1) {
                    pref.setLastWord(word);
                    current = getMaxStateCount();
                    load.setCurrent(current);
                    load.setTotal(current);
                    Timber.v("%d Next Common Word = %s", current, word.toString());
                    ex.postToUi(() -> postResult(item));
                    AndroidUtil.sleep(3);
                }
            }
            if (commonWords.isEmpty()) {
                pref.commitCommonLoaded();
                pref.clearLastWord();
            }
            return item;
        });
    }

    private Maybe<LoadItem> getAlphaItemsRxV2() {
        return Maybe.fromCallable(() -> {
            LoadItem item = LoadItem.getSimpleItem();
            if (pref.isAlphaLoaded()) {
                int current = getMaxStateCount();
                Load load = new Load(current, current);
                item.setItem(load);
                return item;
            }

            AppExecutors ex = getEx();
            if (alphaWords.size() != Constants.Count.WORD_ALPHA) {
                List<Word> words = repo.getAlphaWords();
                alphaWords.clear();
                alphaWords.addAll(words);
            }
            Timber.v("Cache Alpha Words (%d)", alphaWords.size());
            Word last = pref.getLastWord();
            int lastIndex = last != null ? alphaWords.indexOf(last) : 0;
            int current = getMaxStateCount();
            Load load = new Load(current, current);
            item.setItem(load);
            ex.postToUi(() -> postResult(item));
            Word word;
            if (lastIndex != 0) {
                Timber.v("Removing first %d elements", (lastIndex + 1));
                DataUtil.removeFirst(alphaWords, lastIndex + 1);
            }
            Timber.v("Next final Alpha Words (%d)", alphaWords.size());
            while (!alphaWords.isEmpty()) {
                word = alphaWords.remove(0);
                long result = repo.putItem(word, ItemState.STATE, ItemSubstate.RAW);
                if (result != -1) {
                    pref.setLastWord(word);
                    current = getMaxStateCount();
                    load.setCurrent(current);
                    load.setTotal(current);
                    Timber.v("Next Alpha Word = %s", word.toString());
                    ex.postToUi(() -> postResult(item));
                    AndroidUtil.sleep(3);
                }

            }
            if (alphaWords.isEmpty()) {
                pref.commitAlphaLoaded();
                pref.clearLastWord();
            }
            return item;
        });
    }

    private boolean hasRecent(Word word) {
        if (word.getWord().length() < Constants.Count.WORD_RECENT_LETTER) {
            return false;
        }
/*        if (repo.getStateCount(ItemState.FREQUENT) >= Constants.Count.WORD_RECENT) {
            return false;
        }*/
        if (repo.hasState(word, ItemState.RECENT)) {
            return false;
        }
        return true;
    }

    private int getMaxStateCount() {
        int rawCount = repo.getStateCount(ItemState.STATE, ItemSubstate.RAW);
        int fullCount = repo.getStateCount(ItemState.STATE, ItemSubstate.FULL);
        return rawCount > fullCount ? rawCount : fullCount;
    }
}
