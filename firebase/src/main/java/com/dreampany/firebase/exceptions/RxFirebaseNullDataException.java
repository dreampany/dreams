package com.dreampany.firebase.exceptions;

import android.support.annotation.NonNull;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class RxFirebaseNullDataException extends NullPointerException {

    public RxFirebaseNullDataException() {
    }

    public RxFirebaseNullDataException(@NonNull String message) {
        super(message);
    }
}
