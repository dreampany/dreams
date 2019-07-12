package com.dreampany.word.data.source.room;

import android.graphics.Bitmap;

import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Antonym;
import com.dreampany.word.data.model.Synonym;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;

import java.util.ArrayList;
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
public class RoomWordDataSource implements WordDataSource {

    private final String LIKE = "%";

    private final WordMapper mapper;
    private final WordDao dao;
    private final SynonymDao synonymDao;
    private final AntonymDao antonymDao;

    public RoomWordDataSource(WordMapper mapper,
                              WordDao dao,
                              SynonymDao synonymDao,
                              AntonymDao antonymDao) {
        this.mapper = mapper;
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
        return dao.getCount(word.getId()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Word word) {
        return Maybe.fromCallable(() -> isExists(word));
    }

    @Override
    public long putItem(Word word) {
        long result = dao.insertOrReplace(word);
        if (result != -1) {
            ArrayList<Synonym> synonyms = mapper.getSynonyms(word);
            ArrayList<Antonym> antonyms = mapper.getAntonyms(word);
            if (!DataUtil.isEmpty(synonyms)) {
                synonymDao.insertOrReplace(synonyms);
            }
            if (!DataUtil.isEmpty(antonyms)) {
                antonymDao.insertOrReplace(antonyms);
            }
        }
        return result;
    }

    @Override
    public Maybe<Long> putItemRx(Word word) {
        return Maybe.fromCallable(() -> {
            long result = putItem(word);
            return result;
        });
    }

    @Override
    public List<Long> putItems(List<? extends Word> words) {
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
    public Maybe<List<Long>> putItemsRx(List<? extends Word> words) {
        return Maybe.fromCallable(() -> putItems(words));
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
        return dao.getItem(id);
    }

    @Override
    public Maybe<Word> getItemRx(String id) {
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
    public ArrayList<Word> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public Word getItem(String word, boolean full) {
        Word result = dao.getItem(word);
        if (result != null && full) {
            List<Synonym> synonyms = synonymDao.getItems(result.getId());
            List<Antonym> antonyms = antonymDao.getItems(result.getId());
            result.setSynonyms(mapper.getSynonyms(result, synonyms));
            result.setAntonyms(mapper.getAntonyms(result, antonyms));
        }
        return result;
    }

    @Override
    public Maybe<Word> getItemRx(String word, boolean full) {
        return dao.getItemRx(word);
    }

    @Override
    public List<Word> getItems(List<String> ids) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(List<String> ids) {
        return dao.getItemsRx(ids);
    }

    @Override
    public List<Word> getSearchItems(String query, int limit) {
        return dao.getSearchItems(query, limit);
    }

    @Override
    public ArrayList<Word> getCommonItems() {
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

    @Override
    public List<String> getRawWords() {
        return dao.getRawItems();
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
}
