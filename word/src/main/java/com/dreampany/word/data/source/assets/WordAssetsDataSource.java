package com.dreampany.word.data.source.assets;

import android.content.Context;
import android.graphics.Bitmap;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.FileUtil;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubstate;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Singleton;

import io.reactivex.Maybe;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class WordAssetsDataSource implements WordDataSource {

    private static final String WORDS_COMMON = "words_common.txt";
    private static final String WORDS_ALPHA = "words_alpha.txt";

    private final Context context;
    private final WordMapper mapper;
    private volatile boolean loading;

    private final List<String> alphaWords;

    public WordAssetsDataSource(Context context,
                                WordMapper mapper) {
        this.context = context;
        this.mapper = mapper;
        this.alphaWords = new ArrayList<>();
    }

    @Override
    public boolean hasState(Word word, ItemState state) {
        return false;
    }

    @Override
    public boolean hasState(Word word, ItemState state, ItemSubstate substate) {
        return false;
    }

    @Override
    public int getStateCount(ItemState state, ItemSubstate substate) {
        return 0;
    }

    @Override
    public List<State> getStates(Word word) {
        return null;
    }

    @Override
    public List<State> getStates(Word word, ItemState state) {
        return null;
    }

    @Override
    public long putItem(Word word, ItemState state) {
        return 0;
    }

    @Override
    public long putItem(Word word, ItemState state, ItemSubstate substate) {
        return 0;
    }

    @Override
    public long putItem(Word word, ItemState state, boolean replaceable) {
        return 0;
    }

    @Override
    public long putState(Word word, ItemState state) {
        return 0;
    }

    @Override
    public long putState(Word word, ItemState state, ItemSubstate substate) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Word word, ItemState state) {
        return null;
    }

    @Override
    public Maybe<Long> putItemRx(Word word, ItemState state, ItemSubstate substate) {
        return null;
    }

    @Override
    public Maybe<Long> putStateRx(Word word, ItemState state) {
        return null;
    }

    @Override
    public Word getTodayItem() {
        return null;
    }

    @Override
    public Maybe<Word> getTodayItemRx() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(Word word) {
        List<String> items = getAlphaWords();
        return items.contains(word.getWord());
    }

    @Override
    public Maybe<Boolean> isExistsRx(Word word) {
        return Maybe.fromCallable(() -> isExists(word));
    }

    @Override
    public long putItem(Word word) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Word word) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Word> words) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Word> words) {
        return null;
    }

    @Override
    public Word getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Word> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Word> getItems() {
        List<String> items = getCommonWords();
        List<Word> result = new ArrayList<>();
        Stream.of(Objects.requireNonNull(items)).forEach(item -> {
            Word word = mapper.toItem(item);
            result.add(word);
        });
        return result;
    }

    @Override
    public Maybe<List<Word>> getItemsRx() {
        return Maybe.fromCallable(this::getItems);
    }

    @Override
    public List<Word> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public Word getItem(String word) {
        return null;
    }

    @Override
    public Maybe<Word> getItemRx(String word) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getSearchItemsRx(String query) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getSearchItemsRx(String query, int limit) {
        return null;
    }

    @Override
    public List<Word> getCommonItems() {
        List<String> items = getCommonWords();
        List<Word> result = new ArrayList<>(items.size());
        Stream.of(Objects.requireNonNull(items)).forEach(item -> {
            Word word = mapper.toItem(item);
            result.add(word);
        });
        return result;
    }

    @Override
    public List<Word> getAlphaItems() {
        List<String> items = getAlphaWords();
        List<Word> result = new ArrayList<>(items.size());
        Stream.of(Objects.requireNonNull(items)).forEach(item -> {
            Word word = mapper.toItem(item);
            result.add(word);
        });
        return result;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(Bitmap bitmap) {
        return null;
    }

    synchronized private List<String> getCommonWords() {
        List<String> items = FileUtil.readAssetsAsStrings(context, WORDS_COMMON);
        if (items == null) {
            Timber.v("Assets common words empty");
        }
        return items;
    }

    synchronized private List<String> getAlphaWords() {
        if (DataUtil.isEmpty(alphaWords)) {
            List<String> items = FileUtil.readAssetsAsStrings(context, WORDS_ALPHA);
            alphaWords.addAll(items);
        }
        return alphaWords;
    }
}
