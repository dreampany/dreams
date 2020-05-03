package com.dreampany.framework.ui.callback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface UiCallback<A, F, T, R, X> {

    @NonNull
    A getUiActivity();

    @Nullable
    F getUiFragment();

    void set(@NonNull T t);

    @NonNull
    R get();

    @NonNull
    X getX();

    void execute(@NonNull  T t);
}
