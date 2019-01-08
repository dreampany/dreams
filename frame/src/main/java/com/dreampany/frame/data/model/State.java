package com.dreampany.frame.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {"id", "type", "subtype", "state"}, unique = true)},
        primaryKeys = {"id", "type", "subtype", "state"})
public class State extends Base {

    @NonNull
    private String type;
    @NonNull
    private String subtype;
    @NonNull
    private String state;
    private String substate;

    @Ignore
    public State() {

    }

    @Ignore
    public State(long id, @NonNull String type, @NonNull String subtype, @NonNull String state) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
        this.state = state;
    }

    public State(long id, @NonNull String type, @NonNull String subtype, @NonNull String state, String substate) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
        this.state = state;
        this.substate = substate;
    }

    @Ignore
    private State(Parcel in) {
        super(in);
        type = in.readString();
        subtype = in.readString();
        state = in.readString();
        substate = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(type);
        dest.writeString(subtype);
        dest.writeString(state);
        dest.writeString(substate);
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        State item = (State) in;
        return Objects.equal(id, item.id)
                && Objects.equal(type, item.type)
                && Objects.equal(subtype, item.subtype)
                && Objects.equal(state, item.state)
                && Objects.equal(substate, item.substate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, subtype, state, substate);
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    public void setState(@NonNull String state) {
        this.state = state;
    }

    public void setSubstate(String substate) {
        this.substate = substate;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getSubtype() {
        return subtype;
    }

    @NonNull
    public String getState() {
        return state;
    }

    public String getSubstate() {
        return substate;
    }

    public boolean hasProperty(String type, String subtype, String state) {
        return Objects.equal(type, this.type)
                && Objects.equal(subtype, this.subtype)
                && Objects.equal(state, this.state);
    }
}
