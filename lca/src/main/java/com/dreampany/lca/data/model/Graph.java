package com.dreampany.lca.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.Base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class Graph extends Base {

    private String slug;
    private long startTime;
    private long endTime;
    private List<List<Float>> priceBtc;
    private List<List<Float>> priceUsd;
    private List<List<Float>> volumeUsd;

    @Ignore
    public Graph() {
    }

    public Graph(long id) {
        this.id = id;
    }

    @Ignore
    private Graph(Parcel in) {
        super(in);
        slug = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        priceBtc = (List<List<Float>>) in.readSerializable();
        priceUsd = (List<List<Float>>) in.readSerializable();
        volumeUsd = (List<List<Float>>) in.readSerializable();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(slug);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeSerializable((Serializable) priceBtc);
        dest.writeSerializable((Serializable) priceUsd);
        dest.writeSerializable((Serializable) volumeUsd);
    }

    public static final Creator<Graph> CREATOR = new Creator<Graph>() {
        @Override
        public Graph createFromParcel(Parcel in) {
            return new Graph(in);
        }

        @Override
        public Graph[] newArray(int size) {
            return new Graph[size];
        }
    };

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setPriceBtc(List<List<Float>> priceBtc) {
        this.priceBtc = priceBtc;
    }

    public void setPriceUsd(List<List<Float>> priceUsd) {
        this.priceUsd = priceUsd;
    }

    public void setVolumeUsd(List<List<Float>> volumeUsd) {
        this.volumeUsd = volumeUsd;
    }

    public String getSlug() {
        return slug;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<List<Float>> getPriceBtc() {
        return priceBtc;
    }

    public List<List<Float>> getPriceUsd() {
        return priceUsd;
    }

    public List<List<Float>> getVolumeUsd() {
        return volumeUsd;
    }
}
