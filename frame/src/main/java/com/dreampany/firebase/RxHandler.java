package com.dreampany.firebase;

import androidx.annotation.NonNull;

import com.dreampany.firebase.exceptions.RxFirebaseNullDataException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import io.reactivex.MaybeEmitter;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class RxHandler<T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    private final MaybeEmitter<? super T> emitter;

    private RxHandler(MaybeEmitter<? super T> emitter) {
        this.emitter = emitter;
    }

    public static <T> void assignOnTask(MaybeEmitter<? super T> emitter, Task<T> task) {
        RxHandler<T> handler = new RxHandler<>(emitter);
        task.addOnSuccessListener(handler);
        task.addOnFailureListener(handler);
        try {
            task.addOnCompleteListener(handler);
        } catch (Throwable ignored) {
        }
    }


    @Override
    public void onComplete(@NonNull Task<T> task) {
        emitter.onComplete();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (!emitter.isDisposed()) {
            emitter.onError(e);
        }
    }

    @Override
    public void onSuccess(T result) {
        if (result != null) {
            emitter.onSuccess(result);
        } else {
            emitter.onError(new RxFirebaseNullDataException("Observables can't emit null values"));
        }
    }
}
