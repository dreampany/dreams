package com.dreampany.word.data.source.vision;

import android.graphics.Bitmap;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.vision.VisionApi;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawladar Roman on 9/27/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class WordVisionDataSource implements WordDataSource {

    private final WordMapper mapper;
    private final VisionApi vision;

    public WordVisionDataSource(WordMapper mapper,
                                VisionApi vision) {
        this.mapper = mapper;
        this.vision = vision;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return null;
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
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Word word) {
        return null;
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
    public int delete(Word word) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Word word) {
        return null;
    }

    @Override
    public List<Long> delete(List<Word> words) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Word> words) {
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
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx() {
        return null;
    }

    @Override
    public List<Word> getItems(long limit) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(long limit) {
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
    public Word getItem(String word, boolean full) {
        return null;
    }

    @Override
    public Maybe<Word> getItemRx(String word, boolean full) {
        return null;
    }

    @Override
    public List<Word> getSearchItems(String query, int limit) {
        return null;
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
        return Maybe.fromCallable(() -> {
            String text = vision.getText(bitmap);
            List<String> items = TextUtil.getWords(text);
            List<Word> result = new ArrayList<>();
            for (String item : items) {
                result.add(mapper.toItem(item.toLowerCase()));
            }
            return result;
        });
    }
}
