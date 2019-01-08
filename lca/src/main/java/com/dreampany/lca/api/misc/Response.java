package com.dreampany.lca.api.misc;

/**
 * Created by Hawladar Roman on 24/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public abstract class Response<T> {
    protected final T data;

    protected Response(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
