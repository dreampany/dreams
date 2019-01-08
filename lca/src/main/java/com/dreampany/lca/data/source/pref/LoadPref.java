package com.dreampany.lca.data.source.pref;

import android.content.Context;

import com.dreampany.frame.data.source.pref.BasePref;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 10/2/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class LoadPref extends BasePref {

    private static final String DEFAULT_FLAG_COMMITTED = "default_flag_committed";

    @Inject
    LoadPref(Context context) {
        super(context);
    }

    synchronized public void commitDefaultFlag() {
        setPrivately(DEFAULT_FLAG_COMMITTED, true);
    }

    synchronized public boolean isDefaultFlagCommitted() {
        return getPrivately(DEFAULT_FLAG_COMMITTED, false);
    }
}
