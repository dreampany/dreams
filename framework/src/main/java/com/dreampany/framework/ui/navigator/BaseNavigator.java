package com.dreampany.framework.ui.navigator;

/**
 * Created by Hawladar Roman on 5/19/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface BaseNavigator {
    void finishActivity();

    void finishActivityWithResult(int resultCode);

    void startActivityForResult(Class cls, int requestCode);

    void startActivityForResultWithExtra(Class clazz, int requestCode, String extraKey, String extraValue);
}
