package com.dreampany.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import hugo.weaving.DebugLog;
import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public final class RxFirestore {

    private final FirebaseFirestore firestore;

    @Inject
    RxFirestore() {
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
        firestore.setFirestoreSettings(settings);
    }

    /**
     * @param collection
     * @param internalPaths collection of internal paths containing Pair<Document, Collection>
     * @param document
     * @param item
     * @param <T>
     * @return
     */
    public <T> Completable putItemRx(@NonNull String collection,
                                     @Nullable TreeSet<Pair<String, String>> internalPaths,
                                     @NonNull String document, T item) {

        CollectionReference ref = firestore.collection(collection);
        if (internalPaths != null && !internalPaths.isEmpty()) {
            for (Pair<String, String> path : internalPaths) {
                if (path.first != null && path.second != null) {
                    ref = ref.document(path.first).collection(path.second);
                }
            }
        }
        DocumentReference doc = ref.document(document);
        return putItemRx(doc, item);
    }

/*    public <T> Completable putItemRx(String collection, String document, T item) {
        DocumentReference ref = firestore.collection(collection).document(document);
        return putItemRx(ref, item);
    }*/

/*    public <T> Completable putItemRx(@NonNull String collection,
                                     @NonNull String subDocument,
                                     @NonNull String subCollection,
                                     @NonNull String document,
                                     T item) {
        DocumentReference ref = firestore.collection(collection).document(subDocument).collection(subCollection).document(document);
        return putItemRx(ref, item);
    }*/

    public <T> Completable putItemRx(DocumentReference ref, T item) {
        return Completable.create(emitter ->
                RxCompletableHandler.assignOnTask(emitter, ref.set(item, SetOptions.merge()))
        );
    }

    public <T> Maybe<T> getItemRx(String collectionPath, String documentPath, Class<T> clazz) {
        DocumentReference ref = firestore.collection(collectionPath).document(documentPath);
        return getItemRx(ref, clazz);
    }

    @DebugLog
    public <T> Maybe<T> getItemRx(@NonNull String collection,
                                  @NonNull String subDocument,
                                  @NonNull String subCollection,
                                  @Nullable Map<String, Object> equalTo,
                                  @Nullable Map<String, Object> lessThanOrEqualTo,
                                  @Nullable Map<String, Object> greaterThanOrEqualTo,
                                  @NonNull Class<T> clazz) {

        Query ref = firestore.collection(collection).document(subDocument).collection(subCollection);

        if (equalTo != null) {
            for (Map.Entry<String, Object> entry : equalTo.entrySet()) {
                ref = ref.whereEqualTo(entry.getKey(), entry.getValue());
            }
        }
        if (lessThanOrEqualTo != null) {
            for (Map.Entry<String, Object> entry : lessThanOrEqualTo.entrySet()) {
                ref = ref.whereLessThanOrEqualTo(entry.getKey(), entry.getValue());
            }
        }
        if (greaterThanOrEqualTo != null) {
            for (Map.Entry<String, Object> entry : greaterThanOrEqualTo.entrySet()) {
                ref = ref.whereGreaterThanOrEqualTo(entry.getKey(), entry.getValue());
            }
        }
        return getItemRx(ref, clazz);
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

    public <T> Maybe<T> getItemRx(Query ref, Class<T> clazz) {
        return Maybe.create(emitter ->
                ref.get().addOnSuccessListener(snapshot -> {
                    if (emitter.isDisposed()) {
                        throw new IllegalStateException();
                    }
                    if (snapshot.isEmpty()) {
                        emitter.onComplete();
                    } else {
                        emitter.onSuccess(snapshot.getDocuments().get(0).toObject(clazz));
                    }
                }).addOnFailureListener(error -> {
                    if (emitter.isDisposed()) {
                        throw new IllegalStateException();
                    }
                    emitter.onError(error);
                })
        );
    }
}
