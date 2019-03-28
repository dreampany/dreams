package com.dreampany.firebase;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    public <T> Completable setDocument(String collectionPath, String documentPath, T item) {
        DocumentReference ref = firestore.collection(collectionPath).document(documentPath);
        return setDocument(ref, item);
    }

    public <T> Completable setDocument(DocumentReference ref, T item) {
        return Completable.create(emitter ->
                RxCompletableHandler.assignOnTask(emitter, ref.set(item, SetOptions.merge()))
        );
    }

    public <T> Maybe<T> getDocument(String collectionPath, String documentPath, Class<T> clazz) {
        DocumentReference ref = firestore.collection(collectionPath).document(documentPath);
        return getDocument(ref, clazz);
    }

    public <T> Maybe<T> getDocument(DocumentReference ref, Class<T> clazz) {
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
}
