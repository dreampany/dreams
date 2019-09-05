package com.dreampany.framework.util;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class Validator {
    private Validator() {
    }

    public static <T> boolean isNullOrEmpty(T... items) {
        return items == null || items.length <= 0;
    }
}
