package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.api.DataSource;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.model.Ico;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface IcoDataSource extends DataSource<Ico> {

    void clear(IcoStatus status);

    Maybe<List<Ico>> getLiveItemsRx(int limit);

    Maybe<List<Ico>> getUpcomingItemsRx(int limit);

    Maybe<List<Ico>> getFinishedItemsRx(int limit);
}
