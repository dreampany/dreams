package com.dreampany.frame.misc.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hawladar Roman on 7/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MultiException extends Exception {

    private List<Throwable> errors = new ArrayList<>();

    public MultiException(Throwable... errors) {
        this.errors.addAll(Arrays.asList(errors));
    }

    public void add(Throwable exception) {
        errors.add(exception);
    }

    public List<Throwable> getErrors() {
        return errors;
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }
}
