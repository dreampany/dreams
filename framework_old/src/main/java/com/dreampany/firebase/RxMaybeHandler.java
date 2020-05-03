package com.dreampany.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import io.reactivex.MaybeEmitter;
import timber.log.Timber;

/**
 * Created by Roman-372 on 3/28/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class RxMaybeHandler<T> implements OnFailureListener, OnSuccessListener<T>, OnCompleteListener<T> {

    private final MaybeEmitter<T> emitter;

    private RxMaybeHandler(MaybeEmitter<T> emitter) {
        this.emitter = emitter;
    }

    public static <T> void assignOnTask(MaybeEmitter<T> emitter, Task<T> task) {
        RxMaybeHandler<T> handler = new RxMaybeHandler<>(emitter);
        task.addOnFailureListener(handler);
        task.addOnSuccessListener(handler);
        try {
            task.addOnCompleteListener(handler);
        } catch (Throwable error) {
            Timber.e(error);
        }
    }

    @Override
    public void onFailure(@NonNull Exception error) {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onError(error);
        Timber.e("Error %s", error.getMessage());
    }

    @Override
    public void onSuccess(T t) {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onSuccess(t);
        Timber.v("Completed successfully");
    }

    @Override
    public void onComplete(@NonNull Task<T> task) {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onComplete();
        Timber.v("Completed");
    }
}
