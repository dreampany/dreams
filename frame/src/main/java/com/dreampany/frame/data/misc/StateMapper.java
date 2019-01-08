package com.dreampany.frame.data.misc;

import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.StateAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/9/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class StateMapper {

    //keeps only LRU
    private final SmartMap<Long, State> map;
    private final SmartCache<Long, State> cache;

    @Inject
    StateMapper(@StateAnnote SmartMap<Long, State> map,
                @StateAnnote SmartCache<Long, State> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean isExists(long id) {
        return map.contains(id);
    }

    public boolean isExists(long id, String type, String subtype, String state) {
        if (isExists(id)) {
            State item = getItem(id);
            return item.hasProperty(type, subtype, state);
        }
        return false;
    }

    public void putItem(State item) {
        map.put(item.getId(), item);
    }

    public State getItem(long id) {
        return map.get(id);
    }

    public State getItem(long id, String type, String subtype, String state) {
        if (isExists(id, type, subtype, state)) {
            return getItem(id);
        }
        return null;
    }
}
