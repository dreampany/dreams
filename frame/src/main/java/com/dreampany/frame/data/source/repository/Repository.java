package com.dreampany.frame.data.source.repository;

import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;

import java.io.IOException;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 5/30/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public abstract class Repository<K, V> {

    protected final RxMapper rx;
    protected final ResponseMapper rm;
    private final PublishSubject<Response<V>> subject;
    private final PublishSubject<Response<List<V>>> subjects;

    protected Repository(RxMapper rx,
                         ResponseMapper rm) {
        this.rx = rx;
        this.rm = rm;
        subject = PublishSubject.create();
        subjects = PublishSubject.create();
    }

    public PublishSubject<Response<V>> getSubject() {
        return subject;
    }

    public PublishSubject<Response<List<V>>> getSubjects() {
        return subjects;
    }

    @SafeVarargs
    protected final Maybe<V> concatSingleFirstRx(Maybe<V>... sources) {
        return Maybe.create(emitter -> {
            Exception error = null;
            V item = null;

            for (Maybe<V> source : sources) {
                try {
                    item = source.blockingGet();
                } catch (Exception ignored) {
                    error = new IOException();
                }
                if (item != null) {
                    break;
                }
            }
            if (item == null) {
                if (error == null) {
                    error = new EmptyException();
                }
            } else {
                error = null;
            }

            if (!emitter.isDisposed()) {
                if (error != null) {
                    emitter.onError(error);
                } else {
                    emitter.onSuccess(item);
                }
            }
        });
    }


    @SafeVarargs
    protected final Maybe<List<V>> concatFirstRx(Maybe<List<V>>... sources) {
        return Maybe.create(emitter -> {
            Exception error = null;
            List<V> items = null;

            for (Maybe<List<V>> source : sources) {
                try {
                    items = source.blockingGet();
                    Timber.v("Concat %d", items.size());
                } catch (Exception ignored) {
                    error = new IOException();
                }
                if (!DataUtil.isEmpty(items)) {
                    break;
                }
            }
            if (DataUtil.isEmpty(items)) {
                if (error == null) {
                    error = new EmptyException();
                }
            } else {
                error = null;
            }

            if (!emitter.isDisposed()) {
                if (error != null) {
                    emitter.onError(error);
                } else {
                    emitter.onSuccess(items);
                }
            }
        });
    }

    @SafeVarargs
    protected final Flowable<List<V>> concatFirstRx(Flowable<List<V>>... sources) {
        return Flowable.create(emitter -> {
            Exception error = null;
            List<V> items = null;

            for (Flowable<List<V>> source : sources) {
                try {
                    items = source.blockingFirst();
                } catch (Exception ignored) {
                    error = new IOException();
                }
                if (!DataUtil.isEmpty(items)) {
                    break;
                }
            }
            if (DataUtil.isEmpty(items)) {
                if (error == null) {
                    error = new EmptyException();
                }
            } else {
                error = null;
            }

            if (!emitter.isCancelled()) {
                if (error != null) {
                    emitter.onError(error);
                } else {
                    emitter.onNext(items);
                }
            }

        }, BackpressureStrategy.BUFFER);

    }

    @SafeVarargs
    protected final Flowable<List<V>> concatLastRx(Flowable<List<V>>... sources) {
        return Flowable.create(emitter -> {
            Exception error = null;
            List<V> result = null;

            for (Flowable<List<V>> source : sources) {
                List<V> items = null;
                try {
                    items = source.blockingFirst();
                } catch (Exception ignored) {
                    error = new IOException();
                }
                if (!DataUtil.isEmpty(items)) {
                    result = items;
                }
            }
            if (DataUtil.isEmpty(result)) {
                if (error == null) {
                    error = new EmptyException();
                }
            } else {
                error = null;
            }

            if (!emitter.isCancelled()) {
                if (error != null) {
                    emitter.onError(error);
                } else {
                    emitter.onNext(result);
                }
            }

        }, BackpressureStrategy.BUFFER);
    }

    @SafeVarargs
    protected final Maybe<V> concatSingleLastRx(Maybe<V>... sources) {
        return Maybe.create(emitter -> {
            Exception error = null;
            V result = null;

            for (Maybe<V> source : sources) {
                V item = null;
                try {
                    item = source.blockingGet();
                } catch (Exception ignored) {
                    error = new IOException();
                }
                if (item != null) {
                    result = item;
                }
            }
            if (result == null) {
                if (error == null) {
                    error = new EmptyException();
                }
            } else {
                error = null;
            }
            if (!emitter.isDisposed()) {
                if (error != null) {
                    emitter.onError(error);
                } else {
                    emitter.onSuccess(result);
                }
            }
        });
    }

    @SafeVarargs
    protected final Maybe<List<V>> concatLastRx(Maybe<List<V>>... sources) {
        return Maybe.create(emitter -> {
            Exception error = null;
            List<V> result = null;

            for (Maybe<List<V>> source : sources) {
                List<V> items = null;
                try {
                    items = source.blockingGet();
                } catch (Exception ignored) {
                    error = new IOException();
                }
                if (!DataUtil.isEmpty(items)) {
                    result = items;
                }
            }
            if (DataUtil.isEmpty(result)) {
                if (error == null) {
                    error = new EmptyException();
                }
            } else {
                error = null;
            }

            if (!emitter.isDisposed()) {
                if (error != null) {
                    emitter.onError(error);
                } else {
                    emitter.onSuccess(result);
                }
            }
        });
    }

    protected final Maybe<V> contactSingleSuccess(Maybe<V> source, Consumer<V> onSuccess) {
        Maybe<V> maybe = source
                .filter(v -> !DataUtil.isEmpty(v));
        if (onSuccess != null) {
            maybe = maybe.doOnSuccess(onSuccess);
        }
        return maybe;
    }

    protected final Maybe<List<V>> contactSuccess(Maybe<List<V>> source, Consumer<List<V>> onSuccess) {
        Maybe<List<V>> maybe = source
                .filter(vs -> !DataUtil.isEmpty(vs));
        if (onSuccess != null) {
            maybe = maybe.doOnSuccess(onSuccess);
        }
        return maybe;
    }


}
