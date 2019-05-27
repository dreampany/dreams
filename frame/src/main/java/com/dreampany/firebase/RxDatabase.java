package com.dreampany.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dreampany.firebase.exceptions.RxFirebaseDataException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 6/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Singleton
public class RxDatabase {

    @Inject
    RxDatabase() {

    }

    @NonNull
    public static Flowable<DataSnapshot> observeValueEvent(@NonNull final Query query,
                                                           @NonNull BackpressureStrategy strategy) {
        return Flowable.create(emitter -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    emitter.onNext(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (!emitter.isCancelled()) {
                        emitter.onError(new RxFirebaseDataException(error));
                    }
                }
            };
            emitter.setCancellable(() -> query.removeEventListener(listener));
            query.addValueEventListener(listener);
        }, strategy);
    }

    @NonNull
    public static Maybe<DataSnapshot> observeSingleValueEvent(@NonNull final Query query) {
        return Maybe.create(emitter -> query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emitter.onSuccess(dataSnapshot);
                } else {
                    emitter.onComplete();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new RxFirebaseDataException(error));
                }
            }
        }));
    }

    @NonNull
    public static Single<DataSnapshot> runTransaction(@NonNull DatabaseReference ref,
                                                      boolean fireLocalEvents,
                                                      long transactionValue) {
        return Single.create(emitter -> ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(transactionValue);
                } else {
                    mutableData.setValue(currentValue + transactionValue);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null && !emitter.isDisposed()) {
                    emitter.onError(new RxFirebaseDataException(databaseError));
                } else {
                    emitter.onSuccess(dataSnapshot);
                }
            }
        }, fireLocalEvents));
    }

    @NonNull
    public static Completable setValue(@NonNull final DatabaseReference ref,
                                       final Object value) {
        return Completable.create(emitter -> ref.setValue(value)
                .addOnSuccessListener(aVoid -> emitter.onComplete())
                .addOnFailureListener(exception -> {
                    if (!emitter.isDisposed())
                        emitter.onError(exception);
                }));
    }

    @NonNull
    public static Completable updateChildren(@NonNull final DatabaseReference ref,
                                             @NonNull final Map<String, Object> updateData) {
        return Completable.create(emitter ->
                ref.updateChildren(updateData, (error, databaseReference) -> {
                    if (error != null && !emitter.isDisposed()) {
                        emitter.onError(new RxFirebaseDataException(error));
                    } else {
                        emitter.onComplete();
                    }
                }));
    }

    @NonNull
    public static Flowable<RxFirebaseChildEvent<DataSnapshot>> observeChildEvent(
            @NonNull final Query query,
            @NonNull BackpressureStrategy strategy) {
        return Flowable.create(emitter -> {
            final ChildEventListener listener = new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                    emitter.onNext(
                            new RxFirebaseChildEvent<>(dataSnapshot.getKey(), dataSnapshot, previousChildName,
                                    RxFirebaseChildEvent.EventType.ADDED));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                    emitter.onNext(
                            new RxFirebaseChildEvent<>(dataSnapshot.getKey(), dataSnapshot, previousChildName,
                                    RxFirebaseChildEvent.EventType.CHANGED));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    emitter.onNext(new RxFirebaseChildEvent<>(dataSnapshot.getKey(), dataSnapshot,
                            RxFirebaseChildEvent.EventType.REMOVED));
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                    emitter.onNext(
                            new RxFirebaseChildEvent<>(dataSnapshot.getKey(), dataSnapshot, previousChildName,
                                    RxFirebaseChildEvent.EventType.MOVED));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (!emitter.isCancelled())
                        emitter.onError(new RxFirebaseDataException(error));
                }
            };
            emitter.setCancellable(() -> query.removeEventListener(listener));
            query.addChildEventListener(listener);

        }, strategy);
    }

    @NonNull
    public static Flowable<DataSnapshot> observeMultipleSingleValueEvent(@NonNull DatabaseReference... whereRefs) {
        return Maybe.merge(Flowable.fromArray(whereRefs)
                .map((Function<DatabaseReference, MaybeSource<? extends DataSnapshot>>) databaseReference -> observeSingleValueEvent(databaseReference))
        );
    }
}
