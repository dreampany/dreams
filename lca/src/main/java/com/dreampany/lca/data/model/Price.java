package com.dreampany.lca.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.Base;
import com.dreampany.lca.misc.Constants;

/**
 * Created by Hawladar Roman on 8/8/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {Constants.Price.ID}, unique = true)},
        primaryKeys = {Constants.Price.ID})
public class Price extends Base {

    private double price;

    @Ignore
    public Price() {

    }

    public Price(String id, double price) {
        super(id);
        this.price = price;
    }

    @Ignore
    private Price(Parcel in) {
        super(in);
        price = in.readDouble();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(price);
    }

    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
