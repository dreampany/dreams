package com.dreampany.word.data.model;

import androidx.room.Ignore;
import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.Base;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Load extends Base {

    private int current;
    private int total;

    @Ignore
    public Load() {

    }

    public Load(int current, int total) {
        this.current = current;
        this.total = total;
    }

    @Ignore
    private Load(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Load> CREATOR = new Creator<Load>() {
        @Override
        public Load createFromParcel(Parcel in) {
            return new Load(in);
        }

        @Override
        public Load[] newArray(int size) {
            return new Load[size];
        }
    };

    public void setCurrent(int current) {
        this.current = current;
    }

    public int forward() {
        this.current++;
        return this.current;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public int getTotal() {
        return total;
    }
}
