package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.lca.data.model.Price;
import com.dreampany.lca.misc.PriceAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/9/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class PriceMapper {

    private final SmartMap<Long, Price> map;
    private final SmartCache<Long, Price> cache;

    @Inject
    PriceMapper(@PriceAnnote SmartMap<Long, Price> map,
                @PriceAnnote SmartCache<Long, Price> cache) {
        this.map = map;
        this.cache = cache;
    }
}
