/*
package com.dreampany.word.ui.model;

import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.BaseParcel;
import com.dreampany.frame.data.model.Task;
import com.dreampany.word.ui.enums.UiSubtype;
import com.dreampany.word.ui.enums.UiType;

*/
/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 *//*

public class UiTask<T extends BaseParcel> extends Task<T> {

    protected UiType type;
    protected UiSubtype subtype;
    private boolean fullscreen;
    private boolean full;


    public UiTask() {
    }

    public UiTask(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    private UiTask(Parcel in) {
        super(in);
        type = UiType.valueOf(in.readInt());
        subtype = UiSubtype.valueOf(in.readInt());
        fullscreen = in.readByte() != 0;
        full = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(type == null ? -1 : type.ordinal());
        dest.writeInt(subtype == null ? -1 : subtype.ordinal());
        dest.writeByte((byte) (fullscreen ? 1 : 0));
        dest.writeByte((byte) (full ? 1 : 0));
    }

    public static final Creator<UiTask> CREATOR = new Creator<UiTask>() {
        @Override
        public UiTask createFromParcel(Parcel in) {
            return new UiTask(in);
        }

        @Override
        public UiTask[] newArray(int size) {
            return new UiTask[size];
        }
    };

    public void setUiType(UiType type) {
        this.type = type;
    }

    public void setSubtype(UiSubtype subtype) {
        this.subtype = subtype;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public UiType getType() {
        return type;
    }

    public UiSubtype getSubtype() {
        return subtype;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public boolean isFull() {
        return full;
    }
}
*/
