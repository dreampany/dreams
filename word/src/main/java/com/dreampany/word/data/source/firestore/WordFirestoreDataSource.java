package com.dreampany.word.data.source.firestore;

import android.graphics.Bitmap;
import com.dreampany.firebase.RxFirestore;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;
import com.dreampany.word.misc.Constants;
import io.reactivex.Maybe;
import timber.log.Timber;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class WordFirestoreDataSource implements WordDataSource {

    private static final String WORDS = Constants.KEY.WORDS;

    private final NetworkManager network;
    private final RxFirestore firestore;

    public WordFirestoreDataSource(NetworkManager network,
                                   RxFirestore firestore) {
        this.network = network;
        this.firestore = firestore;
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
/*        Maybe<Long> maybe = putItemRx(word);
        try {
           return maybe.blockingGet();
        } catch (Exception error) {
            Timber.e(error);
            return -1;
        }*/
       Throwable error = firestore.setDocument(WORDS, word.getWord(), word).blockingGet();
       if (error == null) {
           return 0;
       }
       return -1;
    }

    @Override
    public Maybe<Long> putItemRx(Word word) {
        return firestore.setDocument(WORDS, word.getWord(), word).toMaybe();
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
    public List<Word> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public Word getItem(String word, boolean full) {
        Word result = getItemRx(word, full).blockingGet();
        return result;
    }

    @Override
    public Maybe<Word> getItemRx(String word, boolean full) {
        return firestore.getDocument(WORDS, word, Word.class);
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
        return null;
    }
}
