package com.dreampany.frame.ui.navigator;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by Hawladar Roman on 5/19/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class Navigator implements BaseNavigator {

    private final WeakReference<Activity> activity;

    public Navigator(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }


    @Override
    public void finishActivity() {

    }

    @Override
    public void finishActivityWithResult(int resultCode) {

    }

    @Override
    public void startActivityForResult(Class cls, int requestCode) {

    }

    @Override
    public void startActivityForResultWithExtra(Class clazz, int requestCode, String extraKey, String extraValue) {

    }
}
