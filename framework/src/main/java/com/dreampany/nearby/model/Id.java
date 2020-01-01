package com.dreampany.nearby.model;

import com.dreampany.nearby.util.NearbyUtil;
import com.google.common.primitives.Longs;

import java.nio.ByteBuffer;

/**
 * Created by Hawladar Roman on 6/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Id {
    public long id;
    public long source;
    public long target;

    public Id() {
        id = NearbyUtil.getSha256();
    }

    public Id(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (Id.class.isInstance(obj)) {
            Id inId = (Id) obj;
            return inId.id == id && inId.source == source && inId.target == target;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Longs.hashCode(id) ^ Longs.hashCode(source) ^ Longs.hashCode(target);
    }

    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(8 + 8 + 8);
        buffer.putLong(id);
        buffer.putLong(source);
        buffer.putLong(target);
        return buffer.array();
    }
}