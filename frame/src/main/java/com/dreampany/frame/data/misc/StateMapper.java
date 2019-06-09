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
    private final SmartMap<String, State> map;
    private final SmartCache<String, State> cache;

    @Inject
    StateMapper(@StateAnnote SmartMap<String, State> map,
                @StateAnnote SmartCache<String, State> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean isExists(String id) {
        return map.contains(id);
    }

    public boolean isExists(String id, String type, String subtype, String state) {
        if (isExists(id)) {
            State item = getItem(id);
            return item.hasProperty(type, subtype, state);
        }
        return false;
    }

    public void putItem(State item) {
        map.put(item.getId(), item);
    }

    public State getItem(String id) {
        return map.get(id);
    }

    public State getItem(String id, String type, String subtype, String state) {
        if (isExists(id, type, subtype, state)) {
            return getItem(id);
        }
        return null;
    }
}
