package com.dreampany.firebase;

import com.google.firebase.database.DatabaseReference;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class RxFirebaseQuery {

    private Maybe<DatabaseReference[]> whereMaybe;

    private RxFirebaseQuery() {
    }

    public static RxFirebaseQuery getInstance() {
        return new RxFirebaseQuery();
    }
}
