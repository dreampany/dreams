package com.dreampany.radio.data.misc;

import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.radio.data.model.Station;
import com.dreampany.radio.misc.StationAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 1/9/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class StationMapper {

    private final SmartMap<Long, Station> map;
    private final SmartCache<Long, Station> cache;

    @Inject
    StationMapper(@StationAnnote SmartMap<Long, Station> map,
                  @StationAnnote SmartCache<Long, Station> cache) {
        this.map = map;
        this.cache = cache;
    }
}
