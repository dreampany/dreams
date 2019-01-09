package com.dreampany.radio.data.source.room;

import com.dreampany.radio.data.misc.StationMapper;
import com.dreampany.radio.data.model.Station;
import com.dreampany.radio.data.source.api.StationDataSource;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by Hawladar Roman on 1/9/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Singleton
public class StationRoomDataSource implements StationDataSource {

    private final StationMapper mapper;
    private final StationDao dao;

    public StationRoomDataSource(StationMapper mapper,
                                 StationDao dao) {
        this.mapper = mapper;
        this.dao = dao;
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
