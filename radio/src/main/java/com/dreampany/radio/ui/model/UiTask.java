package com.dreampany.radio.ui.model;

import android.os.Parcel;

import com.dreampany.frame.data.model.BaseParcel;
import com.dreampany.frame.data.model.Task;
import com.dreampany.radio.ui.enums.UiSubtype;
import com.dreampany.radio.ui.enums.UiType;

/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 */
public class UiTask<T extends BaseParcel> extends Task<T> {

    private final boolean fullscreen;
    protected UiType type;
    protected UiSubtype subtype;

    public UiTask(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    private UiTask(Parcel in) {
        super(in);
        fullscreen = in.readByte() != 0;
        type = UiType.valueOf(in.readInt());
        subtype = UiSubtype.valueOf(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (fullscreen ? 1 : 0));
        dest.writeInt(type == null ? -1 : type.ordinal());
        dest.writeInt(subtype == null ? -1 : subtype.ordinal());
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

    public boolean isFullscreen() {
        return fullscreen;
    }

    public UiType getType() {
        return type;
    }

    public UiSubtype getSubtype() {
        return subtype;
    }
}
