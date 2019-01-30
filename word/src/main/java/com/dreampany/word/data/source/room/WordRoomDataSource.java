package com.dreampany.word.data.source.room;

import android.graphics.Bitmap;

import com.dreampany.frame.data.model.State;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubstate;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class WordRoomDataSource implements WordDataSource {

    private final String LIKE = "%";

    private final WordDao dao;
    private final SynonymDao synonymDao;
    private final AntonymDao antonymDao;

    public WordRoomDataSource(WordMapper mapper,
                              WordDao dao,
                              SynonymDao synonymDao,
                              AntonymDao antonymDao) {
        this.dao = dao;
        this.synonymDao = synonymDao;
        this.antonymDao = antonymDao;
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
        return getCount() == 0;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return dao.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return dao.getCountRx();
    }

    @Override
    public boolean isExists(Word word) {
        return dao.getCount(word.getWord()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Word word) {
        return Maybe.fromCallable(() -> isExists(word));
    }

    @Override
    public long putItem(Word word) {
        return dao.insertOrReplace(word);
    }

    @Override
    public Maybe<Long> putItemRx(Word word) {
        return Maybe.fromCallable(() -> {
            long result = putItem(word);
            return result;
        });
    }

    @Override
    public List<Long> putItems(List<Word> words) {
        List<Long> result = dao.insertOrIgnore(words);
/*        Stream.of(words).forEach(coin -> {
            if (!isExists(coin)) {
                result.add(putItem(coin));
            }
        });*/
        int count = getCount();
        Timber.v("Input Words %d Inserted Words %d total %d", words.size(), result.size(), count);
        return result;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Word> words) {
        return Maybe.fromCallable(() -> putItems(words));
    }

    @Override
    public Word getItem(long id) {
        return dao.getItem(id);
    }

    @Override
    public Maybe<Word> getItemRx(long id) {
        return dao.getItemRx(id);
    }

    @Override
    public List<Word> getItems() {
        return dao.getItems();
    }

    @Override
    public Maybe<List<Word>> getItemsRx() {
        return dao.getItemsRx();
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
        return dao.getItem(word);
    }

    @Override
    public Maybe<Word> getItemRx(String word) {
        return dao.getItemRx(word);
    }

    @Override
    public Maybe<List<Word>> getSearchItemsRx(String query) {
        return dao.getSearchItemsRx(query);
    }

    @Override
    public Maybe<List<Word>> getSearchItemsRx(String query, int limit) {
        return dao.getSearchItemsRx(query, limit);
    }

    @Override
    public List<Word> getCommonItems() {
        return null;
    }

    @Override
    public List<Word> getAlphaItems() {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(Bitmap bitmap) {
        return null;
    }
}
