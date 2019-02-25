package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.misc.CoinAlarmAnnote;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinAlertMapper {

    private final SmartMap<Long, CoinAlert> map;
    private final SmartCache<Long, CoinAlert> cache;

    @Inject
    CoinAlertMapper(@CoinAlarmAnnote SmartMap<Long, CoinAlert> map,
                    @CoinAlarmAnnote SmartCache<Long, CoinAlert> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean hasCoinAlarms() {
        return !map.isEmpty();
    }

    public boolean hasCoinAlarm(String symbol, double dayChange) {
        long id = DataUtil.getSha512(symbol, String.valueOf(dayChange));
        return map.contains(id);
    }

    public void add(CoinAlert coinAlarm) {
        long id = DataUtil.getSha512(coinAlarm.getSymbol(), String.valueOf(coinAlarm.getDayChange()));
        this.add(id, coinAlarm);
    }

    public void add(long id, CoinAlert coinAlarm) {
        map.put(id, coinAlarm);
    }

    public void add(List<CoinAlert> alarms) {
        if (!DataUtil.isEmpty(alarms)) {
            for (CoinAlert coin : alarms) {
                add(coin);
            }
        }
    }

}
