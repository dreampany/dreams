/*
package com.dreampany.frame.data.misc;

import com.dreampany.frame.data.model.Store;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.StoreAnnote;

import javax.inject.Inject;

*/
/**
 * Created by Hawladar Roman on 8/9/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

public class StoreMapper {

    //keeps only LRU
    private final SmartMap<String, Store> map;
    private final SmartCache<String, Store> cache;

    @Inject
    StoreMapper(@StoreAnnote SmartMap<String, Store> map,
                @StoreAnnote SmartCache<String, Store> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean isExists(String id) {
        return map.contains(id);
    }

    public boolean isExists(String id, String type, String subtype) {
        if (isExists(id)) {
            Store item = getItem(id);
            return item.hasProperty(type, subtype);
        }
        return false;
    }

    public void putItem(Store item) {
        map.put(item.getId(), item);
    }

    public Store getItem(String id) {
        return map.get(id);
    }

    public Store getItem(String id, String type, String subtype) {
        if (isExists(id, type, subtype)) {
            return getItem(id);
        }
        return null;
    }
}
*/
