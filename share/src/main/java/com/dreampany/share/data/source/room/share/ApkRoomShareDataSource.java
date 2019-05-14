package com.dreampany.share.data.source.room.share;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.repository.StateRepository;
import com.dreampany.media.data.enums.ItemSubtype;
import com.dreampany.media.data.enums.ItemType;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.source.api.MediaDataSource;
import com.dreampany.share.data.enums.ItemState;
import com.dreampany.share.data.misc.ApkStateMapper;
import com.dreampany.share.data.source.api.ShareDataSource;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class ApkRoomShareDataSource implements ShareDataSource<Apk> {

    private final String TYPE = ItemType.MEDIA.name();
    private final String SUBTYPE = ItemSubtype.APK.name();
    private final String STATE_SHARED = ItemState.SHARED.name();

    private final ApkStateMapper mapper;
    private final StateRepository stateRepo;
    private final Map<Apk, Boolean> shares;

    public ApkRoomShareDataSource(ApkStateMapper mapper,
                                  StateRepository stateRepo) {
        this.mapper = mapper;
        this.stateRepo = stateRepo;
        shares = Maps.newConcurrentMap();
    }

    @Override
    public ShareDataSource<Apk> getThis() {
        return this;
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
    public boolean isShared(Apk apk) {
        if (shares.containsKey(apk)) {
            return shares.get(apk);
        }
        return isExists(apk);
    }

    @Override
    public Maybe<Boolean> isSharedRx(Apk apk) {
        if (shares.containsKey(apk)) {
            return Maybe.fromCallable(() -> shares.get(apk));
        }
        return isExistsRx(apk);
    }

    @Override
    public long putShare(Apk apk) {
        State state = stateRepo.getItem(apk.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return stateRepo.putItem(state);
    }

    @Override
    public Maybe<Long> putShareRx(Apk apk) {
        Maybe<State> Maybe = stateRepo.getItemRx(apk.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return Maybe.map(stateRepo::putItem);
    }

    @Override
    public int getCount() {
        return stateRepo.getCount(TYPE, SUBTYPE, STATE_SHARED);
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return stateRepo.getCountRx(TYPE, SUBTYPE, STATE_SHARED);
    }

    @Override
    public boolean isExists(Apk apk) {
        State state = stateRepo.getItem(apk.getId(), TYPE, SUBTYPE, STATE_SHARED);
        boolean shared = stateRepo.isExists(state);
        shares.put(apk, shared);
        return shared;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Apk apk) {
        Maybe<State> Maybe = stateRepo.getItemRx(apk.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return Maybe.map(state -> {
            boolean shared = stateRepo.isExists(state);
            shares.put(apk, shared);
            return shared;
        });
    }

    @Override
    public long putItem(Apk apk) {
        State state = stateRepo.getItem(apk.getId(), TYPE, SUBTYPE, STATE_SHARED);
        long result =  stateRepo.putItem(state);
        if (result > 0) {
            shares.put(apk, true);
        }
        return result;
    }

    @Override
    public Maybe<Long> putItemRx(Apk apk) {
        Maybe<State> state = stateRepo.getItemRx(apk.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return state.map(stateRepo::putItem);
    }

    @Override
    public List<Long> putItems(List<Apk> apks) {
        List<State> states = new ArrayList<>(apks.size());
        Stream.of(apks).forEach(apk -> states.add(stateRepo.getItem(apk.getId(), TYPE, SUBTYPE, STATE_SHARED)));
        List<Long> result = stateRepo.putItems(states);
        if (apks.size() == result.size()) {
            Stream.of(apks).forEach(apk -> shares.put(apk, true));
        }
        return result;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Apk> apks) {
        return Maybe.fromCallable(() -> putItems(apks));
    }

    @Override
    public Apk getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Apk> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Apk> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Apk>> getItemsRx() {
        return null;
    }

    @Override
    public List<Apk> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Apk>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public List<Apk> getSharedItems(MediaDataSource<Apk> source) {
        return null;
    }

    @Override
    public Maybe<List<Apk>> getSharedItemsRx(MediaDataSource<Apk> source) {
        return stateRepo.getItemsRx(TYPE, SUBTYPE, STATE_SHARED)
                .flatMap((Function<List<State>, MaybeSource<List<Apk>>>) states -> getItemsRx(source, states));
    }

    private Maybe<List<Apk>> getItemsRx(MediaDataSource<Apk> source, List<State> states) {
        return Flowable.fromIterable(states)
                .map(item -> mapper.toItem(item, source))
                .toList()
                .toMaybe();
    }
}
