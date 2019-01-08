package com.dreampany.share.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.nearby.model.Peer;
import com.dreampany.share.data.model.User;
import com.dreampany.share.misc.UserAnnote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.nio.ByteBuffer;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 9/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class UserMapper {

    private static final String KEY_NAME = "name";

    private final SmartMap<Long, User> map;
    private final SmartCache<Long, User> cache;
    private final Gson exposeGson;

    @Inject
    UserMapper(@UserAnnote SmartMap<Long, User> map,
               @UserAnnote SmartCache<Long, User> cache) {
        this.map = map;
        this.cache = cache;
        exposeGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public boolean isExists(User item) {
        return map.contains(item.getId());
    }

    public User toItem(Peer in) {
        if (in == null) {
            return null;
        }

        long id = in.getId();
        User out = map.get(id);
        if (out == null) {
            out = new User();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        return out;
    }


    public Peer toItem(User in) {
        return null;
    }

    public byte[] toData(User user) {
        return exposeGson.toJson(user).getBytes();


/*        byte[] name = user.getName().getBytes();
        ByteBuffer buf = ByteBuffer.allocate(1 + 4 + name.length);
        buf.put(KEY_NAME).putInt(name.length).put(name);
        return buf.array();*/
/*
        JsonObject json = new JsonObject();
        json.addProperty(KEY_NAME, user.getName());
        return json.toString().getBytes();*/

    }

    private void resolveUser(User user, byte[] peerData) {
        User result = exposeGson.fromJson(new String(peerData), User.class);
        user.setName(result.getName());
    }

/*    private void resolveUser(byte[] peerData) {
        ByteBuffer buf = ByteBuffer.wrap(peerData);
        buf.get();
        int nameSize = buf.getInt();
        buf.g
    }*/
}
