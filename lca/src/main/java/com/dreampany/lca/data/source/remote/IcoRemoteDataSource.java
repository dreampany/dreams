package com.dreampany.lca.data.source.remote;

import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.api.iwl.model.ApiIco;
import com.dreampany.lca.api.iwl.model.IcoResponse;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.misc.IcoMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.data.source.api.IcoDataSource;
import com.dreampany.lca.misc.IcoWatchList;
import com.dreampany.network.manager.NetworkManager;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import retrofit2.Response;
import timber.log.Timber;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class IcoRemoteDataSource implements IcoDataSource {

    private final NetworkManager network;
    private final IcoMapper mapper;
    private final IcoService service;

    public IcoRemoteDataSource(NetworkManager network,
                               IcoMapper mapper,
                               @IcoWatchList IcoService service) {
        this.network = network;
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(Ico ico) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Ico ico) {
        return null;
    }

    @Override
    public long putItem(Ico ico) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Ico ico) {
        return null;
    }

    @Override
    public List<Long> putItems(List<? extends Ico> icos) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Ico> icos) {
        return null;
    }

    @Override
    public int delete(Ico ico) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Ico ico) {
        return null;
    }

    @Override
    public List<Long> delete(List<? extends Ico> icos) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Ico> icos) {
        return null;
    }

    @Override
    public Ico getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Ico> getItemRx(String id) {
        return null;
    }

    @Override
    public List<Ico> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getItemsRx() {
        return null;
    }

    @Override
    public List<Ico> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getItemsRx(int limit) {
        return null;
    }


    @Override
    public List<Ico> getLiveItems(int limit) {
        try {
            Response<IcoResponse> response = service.getLiveItemsRx().execute();
            if (response.isSuccessful()) {
                IcoResponse result = response.body();
                return getItemsRx(IcoStatus.LIVE, result).blockingGet();
            }
        } catch (IOException | RuntimeException e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public Maybe<List<Ico>> getLiveItemsRx(int limit) {
        return Maybe.create(emitter -> {
            List<Ico> result = getLiveItems(limit);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Ico> getUpcomingItems(int limit) {
        try {
            Response<IcoResponse> response = service.getUpcomingItemsRx().execute();
            if (response.isSuccessful()) {
                IcoResponse result = response.body();
                return getItemsRx(IcoStatus.UPCOMING, result).blockingGet();
            }
        } catch (IOException | RuntimeException e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public Maybe<List<Ico>> getUpcomingItemsRx(int limit) {
        return Maybe.create(emitter -> {
            List<Ico> result = getUpcomingItems(limit);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Ico> getFinishedItems(int limit) {
        try {
            Response<IcoResponse> response = service.getFinishedItemsRx().execute();
            if (response.isSuccessful()) {
                IcoResponse result = response.body();
                return getItemsRx(IcoStatus.FINISHED, result).blockingGet();
            }
        } catch (IOException | RuntimeException e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public Maybe<List<Ico>> getFinishedItemsRx(int limit) {
        return Maybe.create(emitter -> {
            List<Ico> result = getFinishedItems(limit);
            if (emitter.isDisposed()) {
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    /* private api */
    private Maybe<List<Ico>> getItemsRx(IcoStatus status, IcoResponse response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        Collection<ApiIco> items = null;
        switch (status) {
            case LIVE:
                items = response.getLiveIcos();
                break;
            case UPCOMING:
                items = response.getUpcomingIcos();
                break;
            case FINISHED:
                items = response.getFinishedIcos();
                break;
        }

        return Flowable.fromIterable(items)
                .map(in -> mapper.toIco(in, status))
                .toList()
                .toMaybe();
    }
}
