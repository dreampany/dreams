package com.dreampany.framework.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

import static android.util.Log.ERROR;
import static android.util.Log.WARN;

/**
 * Created by Hawladar Roman on 6/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if (priority == ERROR || priority == WARN) {

        }
    }
}
