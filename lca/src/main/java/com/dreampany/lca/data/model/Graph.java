package com.dreampany.lca.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

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

    private String websiteSlug;
    private long startTime;
    private long endTime;
    private List<List<Float>> priceBTC;
    private List<List<Float>> priceUSD;
    private List<List<Float>> volumeUSD;

    @Ignore
    public Graph() {
    }

    public Graph(long id) {
        this.id = id;
    }

/*    public Graph(List<List<Float>> priceBTC, List<List<Float>> priceUSD, List<List<Float>> volumeUSD) {
        this.priceBTC = priceBTC;
        this.priceUSD = priceUSD;
        this.volumeUSD = volumeUSD;
    }*/

    @Ignore
    private Graph(Parcel in) {
        super(in);
        websiteSlug = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        priceBTC = (List<List<Float>>) in.readSerializable();
        priceUSD = (List<List<Float>>) in.readSerializable();
        volumeUSD = (List<List<Float>>) in.readSerializable();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(websiteSlug);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeSerializable((Serializable) priceBTC);
        dest.writeSerializable((Serializable) priceUSD);
        dest.writeSerializable((Serializable) volumeUSD);
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

    public void setWebsiteSlug( String websiteSlug) {
        this.websiteSlug = websiteSlug;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setPriceBTC(List<List<Float>> priceBTC) {
        this.priceBTC = priceBTC;
    }

    public void setPriceUSD(List<List<Float>> priceUSD) {
        this.priceUSD = priceUSD;
    }

    public void setVolumeUSD(List<List<Float>> volumeUSD) {
        this.volumeUSD = volumeUSD;
    }

    public String getWebsiteSlug() {
        return websiteSlug;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<List<Float>> getPriceBTC() {
        return priceBTC;
    }

    public List<List<Float>> getPriceUSD() {
        return priceUSD;
    }

    public List<List<Float>> getVolumeUSD() {
        return volumeUSD;
    }
}
