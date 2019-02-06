package com.dreampany.firebase.exceptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class RxFirebaseNullDataException extends NullPointerException {
    private final static String DEFAULT_MESSAGE = "Task result was successfully but data was empty";

    public RxFirebaseNullDataException() {
    }

    public RxFirebaseNullDataException(@NonNull String detailMessage) {
        super(detailMessage);
    }

    public RxFirebaseNullDataException(@Nullable Exception resultException) {
        super(resultException != null ? resultException.getMessage() : DEFAULT_MESSAGE);
    }
}
