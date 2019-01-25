package com.dreampany.frame.ui.callback;

import androidx.annotation.NonNull;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface UiCallback<A, F, T, R, X> {

    @NonNull
    A getUiActivity();

    @NonNull
    F getUiFragment();

    void set(@NonNull T t);

    @NonNull
    R get();

    @NonNull
    X getX();
}
