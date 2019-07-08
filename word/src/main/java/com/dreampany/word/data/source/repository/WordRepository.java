package com.dreampany.word.data.source.repository;

import android.graphics.Bitmap;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.*;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;
import com.dreampany.word.data.source.pref.Pref;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class WordRepository extends Repository<String, Word> implements WordDataSource {

    private final WordMapper mapper;
    private final Pref pref;
    private final WordDataSource assets;
    private final WordDataSource room;
    private final WordDataSource firestore;
    private final WordDataSource remote;
    private final WordDataSource vision;

    @Inject
    WordRepository(RxMapper rx,
                   ResponseMapper rm,
                   WordMapper mapper,
                   Pref pref,
                   @Assets WordDataSource assets,
                   @Room WordDataSource room,
                   @Firestore WordDataSource firestore,
                   @Remote WordDataSource remote,
                   @Vision WordDataSource vision) {
        super(rx, rm);
        this.mapper = mapper;
        this.pref = pref;
        this.assets = assets;
        this.room = room;
        this.firestore = firestore;
        this.remote = remote;
        this.vision = vision;
    }

    @Override
    public Word getTodayItem() {
        return null;
    }

    @Override
    public Maybe<Word> getTodayItemRx() {
/*        Maybe<Word> room = getTodayFromRoom();
        Maybe<Word> generated = saveState(this.generateToday(), ItemState.TODAY);
        Maybe<Word> single = concatSingleFirstRx(room, generated);
        return single
                .filter(item -> item != null)
                .map(word -> {
*//*                    if (!hasState(word, ItemState.TODAY, ItemSubstate.FULL)) {
                        return getItemRx(word.getWord()).blockingGet();
                    }*//*
                    return word;
                });*/
        return Maybe.empty();
    }

    @Override
    public boolean isEmpty() {
        return room.isEmpty();
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return room.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return room.getCountRx();
    }

    @Override
    public boolean isExists(Word word) {
        boolean exists = room.isExists(word);
        if (!exists) {
            exists = assets.isExists(word);
        }
        return exists;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Word word) {
        return Maybe.fromCallable(() -> isExists(word));
    }

    @Override
    public long putItem(Word word) {
        return room.putItem(word);
    }

    @Override
    public Maybe<Long> putItemRx(Word word) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Word> words) {
        return room.putItems(words);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Word> words) {
        return room.putItemsRx(words);
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
        return room.getItem(id);
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
        Maybe<List<Word>> assets = getAssetsItemsIfRx();
        Maybe<List<Word>> room = this.room.getItemsRx();
        return concatLastRx(assets, room);
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
        Word result = room.getItem(word, full);
        if (result == null) {
            result = assets.getItem(word, full);
        }
        return result;
    }

    @Override
    public Maybe<Word> getItemRx(String word, boolean full) {
/*        Maybe<Word> room = fullRoom(word);
        Maybe<Word> firestore = saveRoom(this.firestore.getItemRx(word));
        Maybe<Word> remote = saveRoomFirestore(this.remote.getItemRx(word));
        return concatSingleFirstRx(room, firestore, remote);*/
        return Maybe.empty();
    }

    @Override
    public List<Word> getItems(List<String> ids) {
        return room.getItems(ids);
    }

    @Override
    public Maybe<List<Word>> getItemsRx(List<String> ids) {
        return room.getItemsRx(ids);
    }

    @Override
    public List<Word> getSearchItems(String query, int limit) {
        return room.getSearchItems(query, limit);
    }

    @Override
    public List<Word> getCommonItems() {
        return assets.getCommonItems();
    }

    @Override
    public List<Word> getAlphaItems() {
        return assets.getAlphaItems();
    }

    @Override
    public Maybe<List<Word>> getItemsRx(Bitmap bitmap) {
        return null;
    }

    @Override
    public List<String> getRawWords() {
        return room.getRawWords();
    }

    @Override
    public Maybe<List<String>> getRawWordsRx() {
        Maybe<List<String>> assets = this.assets.getRawWordsRx();
        Maybe<List<String>> room = this.room.getRawWordsRx();
        return concatFirstOfStringRx(room, assets);
    }


    public Word getRoomItem(String word, boolean full) {
        return room.getItem(word, full);
    }

    public Word getFirestoreItem(String word, boolean full) {
        return firestore.getItem(word, full);
    }

    public Word getRemoteItem(String word, boolean full) {
        return remote.getItem(word, full);
    }

    public long putRoomItem(Word word) {
       return room.putItem(word);
    }

    public long putFirestoreItem(Word word) {
        return firestore.putItem(word);
    }

    public long putRemoteItem(Word word) {
        return remote.putItem(word);
    }

    public Word getItemOfMapper(String word) {
        return mapper.toItem(word);
    }

    public Word getItemOf(String word, boolean full) {
        Word result = room.getItem(word, full);
        if (result == null) {
            result = getItemOfMapper(word);
        }
        return result;
    }

    /* private api */
    private Maybe<List<Word>> getAssetsItemsIfRx() {
        return Maybe.fromCallable(() -> {
            if (getCount() == 0) {
                Maybe<List<Word>> maybe = saveRoomOfItems(assets.getItemsRx());
                return maybe.blockingGet();
            }
            return null;
        });
    }

    private Maybe<List<Word>> getItemsByStatesRx(List<State> items) {
        return Flowable.fromIterable(items)
                .map(item -> mapper.toItem(item, room))
                .toList()
                .toMaybe();
    }


    private Maybe<List<Word>> saveRoomOfItems(Maybe<List<Word>> source) {
        return source
                .filter(items -> !(DataUtil.isEmpty(items)))
                .doOnSuccess(words -> {
                    rx.compute(putItemsRx(words)).subscribe();
                });
    }

/*    private Maybe<Word> getTodayFromRoom() {
        String type = ItemType.WORD.name();
        String subtype = ItemSubtype.DEFAULT.name();
        String state = ItemState.TODAY.name();
        long from = TimeUtil.startOfDay();
        long to = TimeUtil.endOfDay() - 1;
        return stateRepo.getItemOrderByRx(type, subtype, state, from, to)
                .map(item -> {
                    if (item != null) {
                        return mapper.toItem(item, room);
                    }
                    return null;
                });
    }*/

/*    private Maybe<Word> generateToday() {
        String type = ItemType.WORD.name();
        String subtype = ItemSubtype.DEFAULT.name();
        String state = ItemState.TODAY.name();
        return stateRepo.getItemNotStateOrderByRx(type, subtype, state)
                .map(item -> {
                    if (item != null) {
                        return mapper.toItem(item, room);
                    }
                    return null;
                });
    }*/
}
