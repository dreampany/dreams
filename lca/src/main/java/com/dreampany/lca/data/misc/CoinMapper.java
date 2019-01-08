package com.dreampany.lca.data.misc;

import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cmc.model.CmcCoin;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.CoinAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinMapper {

    private final SmartMap<Long, Coin> map;
    private final SmartCache<Long, Coin> cache;

    @Inject
    CoinMapper(@CoinAnnote SmartMap<Long, Coin> map,
               @CoinAnnote SmartCache<Long, Coin> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean isExists(Coin item) {
        return map.contains(item.getId());
    }

    public Coin toItem(CmcCoin in, boolean full) {
        if (in == null) {
            return null;
        }

        long id = DataUtil.getSha512(in.getSymbol(), in.getSlug());
        Coin out = map.get(id);
        if (out == null) {
            out = new Coin();
            if (full) {
                map.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setCoinId(in.getId());
        out.setName(in.getName());
        out.setSymbol(in.getSymbol());
        out.setSlug(in.getSlug());
        if (full) {
            out.setRank(in.getRank());
            out.setMarketPairs(in.getMarketPairs());
            out.setMaxSupply(in.getCirculatingSupply());
            out.setCirculatingSupply(in.getCirculatingSupply());
            out.setTotalSupply(in.getTotalSupply());
            out.setMaxSupply(in.getMaxSupply());
            out.setLastUpdated(in.getLastUpdatedTime());
            out.setDateAdded(in.getDateAddedTime());
            out.setTags(in.getTags());
            out.setPriceQuote(in.getPriceQuote());
        }
        return out;
    }

    public Coin toItem(Flag flag, CoinDataSource source) {
        Coin coin = map.get(flag.getId());
        if (coin == null) {
            coin = source.getItem(flag.getId());
            map.put(coin.getId(), coin);
        }
        return coin;
    }
}
