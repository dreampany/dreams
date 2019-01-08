package com.dreampany.lca.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dreampany.frame.data.model.Base;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.source.room.IcoStatusConverter;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class Ico extends Base {

    private String name;
    private String imageUrl;
    private String description;
    private String websiteLink;
    private String icoWatchListUrl;
    private String startTime;
    private String endTime;
    private String timezone;
    private String coinSymbol;
    private String priceUSD;
    private String allTimeRoi;
    @TypeConverters(IcoStatusConverter.class)
    private IcoStatus status;

    @Ignore
    public Ico() {

    }

    public Ico(long id) {
        this.id = id;
    }

/*    public Ico(String name, String imageUrl, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }*/

    @Ignore
    private Ico(Parcel in) {
        super(in);
        name = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        websiteLink = in.readString();
        icoWatchListUrl = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        timezone = in.readString();
        coinSymbol = in.readString();
        priceUSD = in.readString();
        allTimeRoi = in.readString();
        status = in.readParcelable(IcoStatus.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(websiteLink);
        dest.writeString(icoWatchListUrl);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(timezone);
        dest.writeString(coinSymbol);
        dest.writeString(priceUSD);
        dest.writeString(allTimeRoi);
        dest.writeParcelable(status, flags);
    }

    public static final Creator<Ico> CREATOR = new Creator<Ico>() {
        @Override
        public Ico createFromParcel(Parcel in) {
            return new Ico(in);
        }

        @Override
        public Ico[] newArray(int size) {
            return new Ico[size];
        }
    };

    @Override
    public String toString() {
        return "ICO{" +
                ", name='" + name + '\'' +
                ", endTime=" + endTime +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public void setIcoWatchListUrl( String icoWatchListUrl) {
        this.icoWatchListUrl = icoWatchListUrl;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public void setPriceUSD(String priceUSD) {
        this.priceUSD = priceUSD;
    }

    public void setAllTimeRoi(String allTimeRoi) {
        this.allTimeRoi = allTimeRoi;
    }

    public void setStatus(IcoStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public String getIcoWatchListUrl() {
        return icoWatchListUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public String getPriceUSD() {
        return priceUSD;
    }

    public String getAllTimeRoi() {
        return allTimeRoi;
    }

    public IcoStatus getStatus() {
        return status;
    }
}
