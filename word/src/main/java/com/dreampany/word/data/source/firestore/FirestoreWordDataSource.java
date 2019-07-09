package com.dreampany.word.data.source.firestore;

import android.graphics.Bitmap;

import com.dreampany.firebase.RxFirebaseFirestore;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;
import com.dreampany.word.misc.Constants;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class FirestoreWordDataSource implements WordDataSource {

    private static final String WORDS = Constants.FirebaseKey.WORDS;

    private final NetworkManager network;
    private final RxFirebaseFirestore firestore;

    public FirestoreWordDataSource(NetworkManager network,
                                   RxFirebaseFirestore firestore) {
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
       Throwable error = firestore.setItemRx(WORDS, word.getId(), word).blockingGet();
       if (error == null) {
           return 0;
       }
       return -1;
    }

    @Override
    public Maybe<Long> putItemRx(Word word) {
        //return firestore.setItemRx(WORDS, word.getWord(), word).toMaybe();
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
    public Word getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Word> getItemRx(String id) {
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
        return firestore.getItemRx(WORDS, word, Word.class);
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

    @Override
    public List<String> getRawWords() {
        return null;
    }

    @Override
    public Maybe<List<String>> getRawWordsRx() {
        return null;
    }

}
