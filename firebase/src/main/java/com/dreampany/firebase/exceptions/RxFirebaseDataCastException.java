package com.dreampany.firebase.exceptions;

import android.support.annotation.NonNull;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class RxFirebaseDataCastException extends Exception {

    public RxFirebaseDataCastException() {
    }

    public RxFirebaseDataCastException(@NonNull String message) {
        super(message);
    }

    public RxFirebaseDataCastException(@NonNull String message, @NonNull Throwable throwable) {
        super(message, throwable);
    }

    public RxFirebaseDataCastException(@NonNull Throwable throwable) {
        super(throwable);
    }
}