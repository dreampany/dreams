package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.DataSource;
import com.dreampany.lca.data.model.Graph;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface GraphDataSource extends DataSource<Graph> {

    Graph getItem(String slug, long startTime, long endTime);

    Maybe<Graph> getItemRx(String slug, long startTime, long endTime);
}
