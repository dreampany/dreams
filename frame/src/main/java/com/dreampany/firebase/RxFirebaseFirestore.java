package com.dreampany.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public final class RxFirebaseFirestore {

    private final FirebaseFirestore firestore;

    @Inject
    RxFirebaseFirestore() {
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
        firestore.setFirestoreSettings(settings);
    }

    /**
     * @param collection
     * @param internalPaths collection of internal paths containing MutablePair<Document, Collection>
     * @param document
     * @param item
     * @param <T>
     * @return
     */
    public <T> Completable setItemRx(@NonNull String collection,
                                     @Nullable TreeSet<MutablePair<String, String>> internalPaths,
                                     @NonNull String document,
                                     @NonNull T item) {

        CollectionReference ref = firestore.collection(collection);
        if (internalPaths != null && !internalPaths.isEmpty()) {
            for (MutablePair<String, String> path : internalPaths) {
                if (path.left != null && path.right != null) {
                    ref = ref.document(path.left).collection(path.right);
                }
            }
        }
        DocumentReference doc = ref.document(document);
        return setItemRx(doc, item);
    }

    public <T> Completable setItemsRx(@NonNull String collection,
                                      @NonNull Map<String, MutableTriple<String, String, T>> items) {

        CollectionReference ref = firestore.collection(collection);

        WriteBatch batch = firestore.batch();
        for (Map.Entry<String, MutableTriple<String, String, T>> entry : items.entrySet()) {
            String document = entry.getKey();
            MutableTriple<String, String, T> item = entry.getValue();
            CollectionReference childCollection = ref.document(item.left).collection(item.middle);
            DocumentReference doc = childCollection.document(document);
            batch.set(doc, item.right, SetOptions.merge());
        }
        return atomicOperation(batch);
    }

    public <T> Completable setItemRx(@NonNull DocumentReference ref,
                                     @NonNull T item) {
        return Completable.create(emitter ->
                RxCompletableHandler.assignOnTask(emitter, ref.set(item, SetOptions.merge()))
        );
    }

    public <T> Completable atomicOperation(@NonNull WriteBatch batch) {
        return Completable.create(emitter -> RxCompletableHandler.assignOnTask(emitter, batch.commit()));
    }

    public <T> Maybe<T> getItemRx(@NonNull String collectionPath,
                                  @NonNull String documentPath,
                                  @NonNull Class<T> clazz) {
        DocumentReference ref = firestore.collection(collectionPath).document(documentPath);
        return getItemRx(ref, clazz);
    }

    @DebugLog
    public <T> Maybe<T> getItemRx(@NonNull String collection,
                                  @Nullable TreeSet<MutablePair<String, String>> internalPaths,
                                  @Nullable List<MutablePair<String, Object>> equalTo,
                                  @Nullable List<MutablePair<String, Object>> lessThanOrEqualTo,
                                  @Nullable List<MutablePair<String, Object>> greaterThanOrEqualTo,
                                  @NonNull Class<T> clazz) {

        CollectionReference ref = firestore.collection(collection);
        if (internalPaths != null && !internalPaths.isEmpty()) {
            for (MutablePair<String, String> path : internalPaths) {
                if (path.left != null && path.right != null) {
                    ref = ref.document(path.left).collection(path.right);
                }
            }
        }

        Query query = ref;
        if (equalTo != null) {
            for (MutablePair<String, Object> entry : equalTo) {
                query = query.whereEqualTo(entry.getKey(), entry.getValue());
            }
        }
        if (lessThanOrEqualTo != null) {
            for (MutablePair<String, Object> entry : lessThanOrEqualTo) {
                query = query.whereLessThanOrEqualTo(entry.getKey(), entry.getValue());
            }
        }
        if (greaterThanOrEqualTo != null) {
            for (MutablePair<String, Object> entry : greaterThanOrEqualTo) {
                query = query.whereGreaterThanOrEqualTo(entry.getKey(), entry.getValue());
            }
        }
        return getItemRx(ref, clazz);
    }

    @DebugLog
    public <T> Maybe<List<T>> getItemsRx(@NonNull String collection,
                                         @Nullable TreeSet<MutablePair<String, String>> internalPaths,
                                         @Nullable List<MutablePair<String, Object>> equalTo,
                                         @Nullable List<MutablePair<String, Object>> lessThanOrEqualTo,
                                         @Nullable List<MutablePair<String, Object>> greaterThanOrEqualTo,
                                         @NonNull Class<T> clazz) {

        CollectionReference ref = firestore.collection(collection);
        if (internalPaths != null && !internalPaths.isEmpty()) {
            for (MutablePair<String, String> path : internalPaths) {
                if (path.left != null && path.right != null) {
                    ref = ref.document(path.left).collection(path.right);
                }
            }
        }

        Query query = ref;
        if (equalTo != null) {
            for (MutablePair<String, Object> entry : equalTo) {
                query = query.whereEqualTo(entry.getKey(), entry.getValue());
            }
        }
        if (lessThanOrEqualTo != null) {
            for (MutablePair<String, Object> entry : lessThanOrEqualTo) {
                query = query.whereLessThanOrEqualTo(entry.getKey(), entry.getValue());
            }
        }
        if (greaterThanOrEqualTo != null) {
            for (MutablePair<String, Object> entry : greaterThanOrEqualTo) {
                query = query.whereGreaterThanOrEqualTo(entry.getKey(), entry.getValue());
            }
        }
        return getItemsRx(ref, clazz);
    }

    public <T> Maybe<T> getItemRx(DocumentReference ref, Class<T> clazz) {
        return Maybe.create(emitter ->
                ref.get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        emitter.onSuccess(snapshot.toObject(clazz));
                    } else {
                        emitter.onComplete();
                    }
                }).addOnFailureListener(e -> {
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                })
        );
    }

    public <T> Maybe<T> getItemRx(@NonNull Query ref, @NonNull Class<T> clazz) {
        return Maybe.create(emitter ->
                ref.get().addOnSuccessListener(snapshot -> {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    if (snapshot.isEmpty()) {
                        emitter.onComplete();
                    } else {
                        emitter.onSuccess(snapshot.getDocuments().get(0).toObject(clazz));
                    }
                }).addOnFailureListener(error -> {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    emitter.onError(error);
                })
        );
    }

    public <T> Maybe<List<T>> getItemsRx(@NonNull Query ref, @NonNull Class<T> clazz) {
        return Maybe.create(emitter ->
                ref.get().addOnSuccessListener(snapshot -> {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    if (snapshot.isEmpty()) {
                        emitter.onComplete();
                    } else {
                        emitter.onSuccess(snapshot.toObjects(clazz));
                    }
                }).addOnFailureListener(error -> {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    emitter.onError(error);
                })
        );
    }
}
