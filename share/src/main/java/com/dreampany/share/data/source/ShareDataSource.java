package com.dreampany.share.data.source;

import com.dreampany.frame.data.source.DataSource;
import com.dreampany.media.data.model.Media;
import com.dreampany.media.data.source.api.MediaDataSource;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface ShareDataSource<T extends Media> extends DataSource<T> {

    @Override
    ShareDataSource<T> getThis();

    boolean isShared(T t);

    Maybe<Boolean> isSharedRx(T t);

    long putShare(T t);

    Maybe<Long> putShareRx(T t);

    List<T> getSharedItems(MediaDataSource<T> source);

    Maybe<List<T>> getSharedItemsRx(MediaDataSource<T> source);
}
