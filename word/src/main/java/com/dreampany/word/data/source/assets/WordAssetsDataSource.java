package com.dreampany.word.data.source.assets;

import android.content.Context;
import android.graphics.Bitmap;

import com.annimon.stream.Stream;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.FileUtil;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;
import com.dreampany.word.misc.Constants;

import io.reactivex.Maybe;
import timber.log.Timber;

import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class WordAssetsDataSource implements WordDataSource {

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
        return items.contains(word.getId());
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
    public List<Long> putItems(List<? extends Word> words) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Word> words) {
        return null;
    }

    @Override
    public int delete(Word word) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Word word) {
        return null;
    }

    @Override
    public List<Long> delete(List<? extends Word> words) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Word> words) {
        return null;
    }

    @Override
    public Word getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Word> getItemRx(String id) {
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
    public Word getItem(String word, boolean full) {
        return alphaWords.contains(word) ? mapper.toItem(word) : null;
    }

    @Override
    public Maybe<Word> getItemRx(String word, boolean full) {
        return null;
    }

    @Override
    public List<Word> getItems(List<String> ids) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(List<String> ids) {
        return null;
    }

    @Override
    public List<Word> getSearchItems(String query, int limit) {
        return null;
    }

    @Override
    synchronized public List<Word> getCommonItems() {
        List<String> items = getCommonWords();
        List<Word> result = new ArrayList<>(items.size());
        Stream.of(Objects.requireNonNull(items)).forEach(item -> {
            Word word = mapper.toItem(item);
            result.add(word);
        });
        return result;
    }

    @Override
    synchronized public List<Word> getAlphaItems() {
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

    @Override
    public List<String> getRawWords() {
        return getAlphaWords();
    }

    @Override
    public Maybe<List<String>> getRawWordsRx() {
        return Maybe.create(emitter -> {
            List<String> result = getRawWords();
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


    synchronized private List<String> getCommonWords() {
        List<String> items = FileUtil.readAssetsAsStrings(context, Constants.Assets.WORDS_COMMON);
        if (items == null) {
            Timber.v("Assets common words empty");
        }
        return items;
    }

    synchronized private List<String> getAlphaWords() {
        if (DataUtil.isEmpty(alphaWords)) {
            List<String> items = FileUtil.readAssetsAsStrings(context, Constants.Assets.WORDS_ALPHA);
            alphaWords.addAll(items);
        }
        return new ArrayList<>(alphaWords);
    }
}
