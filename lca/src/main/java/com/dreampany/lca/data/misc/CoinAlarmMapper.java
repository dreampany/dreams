package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.model.CoinAlarm;
import com.dreampany.lca.misc.CoinAlarmAnnote;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinAlarmMapper {

    private final SmartMap<Long, CoinAlarm> map;
    private final SmartCache<Long, CoinAlarm> cache;

    @Inject
    CoinAlarmMapper(@CoinAlarmAnnote SmartMap<Long, CoinAlarm> map,
                    @CoinAlarmAnnote SmartCache<Long, CoinAlarm> cache) {
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

    public void add(CoinAlarm coinAlarm) {
        long id = DataUtil.getSha512(coinAlarm.getSymbol(), String.valueOf(coinAlarm.getDayChange()));
        this.add(id, coinAlarm);
    }

    public void add(long id, CoinAlarm coinAlarm) {
        map.put(id, coinAlarm);
    }

    public void add(List<CoinAlarm> alarms) {
        if (!DataUtil.isEmpty(alarms)) {
            for (CoinAlarm coin : alarms) {
                add(coin);
            }
        }
    }

}
