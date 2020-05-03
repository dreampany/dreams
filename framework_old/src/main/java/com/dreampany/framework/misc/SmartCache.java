package com.dreampany.framework.misc;

import androidx.annotation.NonNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 6/7/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class SmartCache<K, V> {

    @NonNull
    private final Cache<K, V> cache;

    private SmartCache() {
        cache = CacheBuilder.newBuilder()
                .weakKeys()
                .build();
    }

    private SmartCache(int minutes) {
        cache = CacheBuilder.newBuilder()
                .weakKeys()
                .expireAfterWrite(minutes, TimeUnit.MINUTES)
                .build();

    }

    public static <K, V> SmartCache<K, V> newCache() {
        return new SmartCache<>();
    }

    public static <K, V> SmartCache<K, V> newCache(int minutes) {
        return new SmartCache<>(minutes);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.getIfPresent(key);
    }

    public void clear() {
        cache.cleanUp();
    }

    public boolean isEmpty() {
        return cache.size() == 0;
    }
}
