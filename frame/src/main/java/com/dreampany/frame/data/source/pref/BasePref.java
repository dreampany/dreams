package com.dreampany.frame.data.source.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.dreampany.frame.util.AndroidUtil;
import com.facebook.common.logging.FLog;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.github.pwittchen.prefser.library.rx2.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public abstract class BasePref {

    protected Prefser publicPref;
    protected Prefser privatePref;

    protected BasePref(Context context) {
        if (context == null) {
            throw new NullPointerException();
        }

        publicPref = new Prefser(context.getApplicationContext());
        String prefName = AndroidUtil.getApplicationId(context.getApplicationContext());
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        privatePref = new Prefser(pref);
    }

    public boolean isPubliclyAvailable(String key) {
        return privatePref.contains(key);
    }

    public boolean isPrivateAvailable(String key) {
        return publicPref.contains(key);
    }

    public <T> void setListItem(String key, T item) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        List<T> inputs = new ArrayList<>();
        inputs.add(item);
        List<T> items = privatePref.get(key, typeToken, inputs);
        if (!items.contains(item)) {
            items.add(item);
        }
        privatePref.put(key, items, typeToken);
    }

    public <T> void setListItems(String key, List<T> items) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        privatePref.put(key, items, typeToken);
    }

    public <T> List<T> getListItems(String key) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        List<T> inputs = new ArrayList<>();
        return privatePref.get(key, typeToken, inputs);
    }

    public <T> void setPrivately(String key, T value) {
        privatePref.put(key, value);
    }

    public  void setPrivately(String key, String value) {
        privatePref.put(key, value);
    }

    public void removePrivately(String key) {
        privatePref.remove(key);
    }

    public void removePublicly(String key) {
        publicPref.remove(key);
    }

    public void setPublicly(String key, long value) {
        publicPref.put(key, value);
    }

    public void setPrivately(String key, boolean value) {
        privatePref.put(key, value);
    }

    public void setPrivately(String key, int value) {
        privatePref.put(key, value);
    }

    public void setPrivately(String key, long value) {
        privatePref.put(key, value);
    }

    public <T> T getPrivately(String key, Class<T> classOfT, T defaultValue) {
        return privatePref.get(key,classOfT, defaultValue);
    }

    public String getPublicly(String key, String defaultValue) {
        return publicPref.get(key, String.class, defaultValue);
    }

    public String getPrivately(String key, String defaultValue) {
        return privatePref.get(key, String.class, defaultValue);
    }

    public int getPrivately(String key, int defaultValue) {
        return privatePref.get(key, Integer.class, defaultValue);
    }

    public long getPrivately(String key, long defaultValue) {
        return privatePref.get(key, Long.class, defaultValue);
    }

    public float getPrivately(String key, float defaultValue) {
        return privatePref.get(key, Float.class, defaultValue);
    }

    public boolean getPrivately(String key, boolean defaultValue) {
        return privatePref.get(key, Boolean.class, defaultValue);
    }

    public <T> Flowable<T> observePublic(String key, Class<T> classOfT, T defaultValue) {
        return publicPref.observe(key, classOfT, defaultValue).toFlowable(BackpressureStrategy.LATEST);
    }

    public <T> Flowable<T> observePrivate(String key, Class<T> classOfT, T defaultValue) {
        return privatePref.observe(key, classOfT, defaultValue).toFlowable(BackpressureStrategy.LATEST);
    }

}
