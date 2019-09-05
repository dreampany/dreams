package com.dreampany.radio.data.source.repository;

import com.dreampany.framework.data.source.repository.Repository;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.Room;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.radio.data.model.Station;
import com.dreampany.radio.data.source.api.StationDataSource;
import com.dreampany.radio.data.source.pref.Pref;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by Hawladar Roman on 1/9/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class StationRepository extends Repository<Long, Station> implements StationDataSource {

    private final Pref pref;
    private final StationDataSource room;

    @Inject
     StationRepository(RxMapper rx,
                       ResponseMapper rm,
                       Pref pref,
                       @Room StationDataSource room) {
        super(rx, rm);
        this.pref = pref;
        this.room = room;
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
    public boolean isExists(Station station) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Station station) {
        return null;
    }

    @Override
    public long putItem(Station station) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Station station) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Station> stations) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Station> stations) {
        return null;
    }

    @Override
    public Station getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Station> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Station> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Station>> getItemsRx() {
        return null;
    }

    @Override
    public List<Station> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Station>> getItemsRx(int limit) {
        return null;
    }
}
