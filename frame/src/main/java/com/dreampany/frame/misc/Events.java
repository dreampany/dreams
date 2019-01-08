package com.dreampany.frame.misc;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hawladar Roman on 6/25/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class Events {

    public Events() {

    }

    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }
}
