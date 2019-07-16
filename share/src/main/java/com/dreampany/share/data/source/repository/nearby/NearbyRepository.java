package com.dreampany.share.data.source.repository.nearby;

import android.content.Context;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.nearby.NearbyApi;
import com.dreampany.nearby.model.Peer;
import com.dreampany.share.data.model.User;
import com.google.common.collect.Sets;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 9/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class NearbyRepository extends Repository<Long, User> implements NearbyApi.Callback {

    public interface UserCallback {
        void onJoin(User user);

        void onUpdate(User user);

        void onLeave(User user);
    }

    public interface DiscoverCallback {

    }

    private final Context context;
    private final NearbyApi nearby;

    private long serviceId;
    private User owner;

    private int clients;
    private final Set<UserCallback> userCallbacks;
    private final Set<DiscoverCallback> discoverCallbacks;

    @Inject
    NearbyRepository(Context context,
                     RxMapper rx,
                     ResponseMapper rm,
                     NearbyApi nearby) {
        super(rx, rm);
        this.context = context;
        this.nearby = nearby;

        if (owner == null) {
            serviceId = DataUtil.getSha512(AndroidUtil.getApplicationId(context));
            owner = buildOwner();
        }

        nearby.register(this);
        userCallbacks = Sets.newConcurrentHashSet();
        discoverCallbacks = Sets.newConcurrentHashSet();
    }

    @Override
    public void onPeer(Peer peer, Peer.State state) {
        switch (state) {
            case LIVE:
                Stream.of(userCallbacks).forEach(new Consumer<UserCallback>() {
                    @Override
                    public void accept(UserCallback callback) {
                        //callback.onJoin();
                    }
                });
                break;
        }
    }

    @Override
    public void onData(Peer peer, byte[] data) {

    }

    @Override
    public void onStatus(long payloadId, NearbyApi.PayloadState state, long totalBytes, long bytesTransferred) {

    }

    public void register(UserCallback callback) {
        userCallbacks.add(callback);
        start();
    }

    public void register(DiscoverCallback callback) {
        discoverCallbacks.add(callback);
        start();
    }

    public void unregister(UserCallback callback) {
        userCallbacks.remove(callback);
        stop();
    }

    public void unregister(DiscoverCallback callback) {
        discoverCallbacks.remove(callback);
        stop();
    }

    private void start() {
        if (!userCallbacks.isEmpty() || !discoverCallbacks.isEmpty()) {
            nearby.init(context, serviceId, owner.getId());
            nearby.start();
        }
/*        if (clients <= 0) {
            nearby.start();
        }
        clients++;*/
    }

    private void stop() {
        if (userCallbacks.isEmpty() && discoverCallbacks.isEmpty()) {
            nearby.stop();
        }
/*        clients--;
        if (clients <= 0) {
            nearby.stop();
        }*/
    }


    private User buildOwner() {
        User user = new User();
        user.setId(DataUtil.getSha512(AndroidUtil.getAndroidId(context)));
        user.setName(AndroidUtil.getDeviceName());
        return user;
    }
}
