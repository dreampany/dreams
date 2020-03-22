package com.dreampany.ui;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.dreampany.ui.Mode.IDLE;

@IntDef({IDLE, Mode.SINGLE, Mode.MULTI})
@Retention(RetentionPolicy.SOURCE)
public @interface Mode {
    /**
     * - <b>IDLE:</b> Adapter will not keep track of selections.<br>
     * - <b>SINGLE:</b> Select only one per time.<br>
     * - <b>MULTI:</b> Multi selection will be activated.
     */
    int IDLE = 0, SINGLE = 1, MULTI = 2;
}