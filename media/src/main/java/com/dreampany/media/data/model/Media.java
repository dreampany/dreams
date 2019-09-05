package com.dreampany.media.data.model;

import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.framework.data.model.Base;
import com.dreampany.media.data.enums.MediaType;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public abstract class Media extends Base {

    protected String uri;
    protected String thumbUri;
    protected String displayName;
    protected String title;
    protected String mimeType;
    protected long size;
    protected long dateAdded;
    protected long dateModified;
    protected MediaType mediaType;

    protected Media() {
    }

    protected Media(Parcel in) {
        super(in);
        uri = in.readString();
        thumbUri = in.readString();
        displayName = in.readString();
        title = in.readString();
        mimeType = in.readString();
        size = in.readLong();
        dateAdded = in.readLong();
        dateModified = in.readLong();
        mediaType = in.readParcelable(MediaType.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(uri);
        dest.writeString(thumbUri);
        dest.writeString(displayName);
        dest.writeString(title);
        dest.writeString(mimeType);
        dest.writeLong(size);
        dest.writeLong(dateAdded);
        dest.writeLong(dateModified);
        dest.writeParcelable(mediaType, flags);
    }

    @NonNull
    @Override
    public String toString() {
        return "Media{" +
                "id=" + getId() +
                ", displayName='" + displayName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getUri() {
        return uri;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTitle() {
        return title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getSize() {
        return size;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public long getDateModified() {
        return dateModified;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
