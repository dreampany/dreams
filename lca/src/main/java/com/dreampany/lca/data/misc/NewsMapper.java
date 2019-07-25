package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cc.model.CcNews;
import com.dreampany.lca.data.model.News;
import com.dreampany.lca.data.model.SourceInfo;
import com.dreampany.lca.misc.NewsAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NewsMapper {

    private final SmartMap<String, News> map;
    private final SmartCache<String, News> cache;

    @Inject
    NewsMapper(@NewsAnnote SmartMap<String, News> map,
               @NewsAnnote SmartCache<String, News> cache) {
        this.map = map;
        this.cache = cache;
    }

    public void clear() {
        map.clear();
        cache.clear();
    }

    public News toNews(CcNews in) {
        if (in == null) {
            return null;
        }
        String id = in.getGuid();
        News out = map.get(id);
        if (out == null) {
            out = new News();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setNewsId(in.getId());
        out.setGuid(in.getGuid());
        out.setPublishedOn(in.getPublishedOn());
        out.setImageUrl(in.getImageUrl());
        out.setTitle(in.getTitle());
        out.setUrl(in.getUrl());
        out.setSource(in.getSource());
        out.setBody(in.getBody());
        out.setTags(in.getTags());
        out.setCategories(in.getCategories());
        out.setUpVotes(in.getUpVotes());
        out.setDownVotes(in.getDownVotes());
        out.setLanguage(in.getLanguage());
        out.setSourceInfo(toSourceInfo(in.getSourceInfo(), null));
        return out;
    }

    public SourceInfo toSourceInfo(CcNews.SourceInfo in, SourceInfo out) {
        if (in == null) {
            return null;
        }
        if (out == null) {
            out = new SourceInfo();
        }
        out.setId(DataUtil.join(in.getName(), in.getLanguage()));
        out.setName(in.getName());
        out.setLanguage(in.getLanguage());
        out.setImageUrl(in.getImageUrl());
        return out;
    }
}
