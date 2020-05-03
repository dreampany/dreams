package com.dreampany.nearby.model;

import com.google.common.primitives.Longs;

/**
 * Created by Hawladar Roman on 6/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Peer {

    public enum State {
        LIVE, DEAD
    }

    private final long id;
    private byte[] data;

    public Peer(long id) {
        this(id, null);
    }

    public Peer(long id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;

        Peer peer = (Peer) inObject;
        if (id != peer.id) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Longs.hashCode(id);
    }

    @Override
    public String toString() {
        return "peer: \n" + "id - " + id + "\n" +
                "data - " + (data == null ? "null" : new String(data));
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }
}
