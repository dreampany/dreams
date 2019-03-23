package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cmc.model.CmcGraph;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.misc.GraphAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class GraphMapper {

    private final SmartMap<Long, Graph> map;
    private final SmartCache<Long, Graph> cache;

    @Inject
    GraphMapper(@GraphAnnote SmartMap<Long, Graph> map,
                @GraphAnnote SmartCache<Long, Graph> cache) {
        this.map = map;
        this.cache = cache;
    }

    public Graph toGraph(CmcGraph in) {
        if (in == null) {
            return null;
        }

        long id  = DataUtil.getSha512(in.getSlug());
        Graph out = map.get(id);
        if (out == null) {
            out = new Graph();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setSlug(in.getSlug());
        out.setStartTime(in.getStartTime());
        out.setEndTime(in.getEndTime());
        out.setPriceBtc(in.getPriceBtc());
        out.setPriceUsd(in.getPriceUsd());
        out.setVolumeUsd(in.getVolumeUsd());
        return out;
    }

    public boolean hasGraph(String slug) {
        return map.contains(DataUtil.getSha512(slug));
    }

    public Graph getGraph(String slug) {
        long id  = DataUtil.getSha512(slug);
        return map.get(id);
    }

}
