package com.dreampany.lca.api.iwl.model;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public abstract class Response<T> {
    public static final String JSON_ICO_PROPERTY = "ico";
    public static final String JSON_LIVE_ICO_PROPERTY = "live";
    public static final String JSON_UPCOMING_ICO_PROPERTY = "upcoming";
    public static final String JSON_FINISHED_ICO_PROPERTY = "finished";
    protected final T ico;

    public Response(T ico) {
        this.ico = ico;
    }

    public T getIco() {
        return ico;
    }
}
