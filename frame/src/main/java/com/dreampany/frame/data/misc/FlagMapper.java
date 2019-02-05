/*
package com.dreampany.frame.data.misc;

import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.misc.FlagAnnote;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;

import javax.inject.Inject;

*/
/**
 * Created by Hawladar Roman on 8/9/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

public class FlagMapper {

    //keeps only LRU
    private final SmartMap<Long, Flag> map;
    private final SmartCache<Long, Flag> cache;

    @Inject
    FlagMapper(@FlagAnnote SmartMap<Long, Flag> map,
               @FlagAnnote SmartCache<Long, Flag> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean isExists(long id) {
        return map.contains(id);
    }

    public boolean isExists(long id, String type, String subtype) {
        if (isExists(id)) {
            Flag item = getItem(id);
            return item.hasProperty(type, subtype);
        }
        return false;
    }

    public void putItem(Flag item) {
        map.put(item.getId(), item);
    }

    public Flag getItem(long id) {
        return map.get(id);
    }

    public Flag getItem(long id, String type, String subtype) {
        if (isExists(id, type, subtype)) {
            return getItem(id);
        }
        return null;
    }
}
*/
