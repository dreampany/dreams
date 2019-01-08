package com.dreampany.lca.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dreampany.frame.data.model.Base;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class News extends Base {

    private long newsId;
    private String guid;
    private long publishedOn;
    private String imageUrl;
    private String title;
    private String url;
    private String source;
    private String body;
    private String tags;
    private String categories;
    private long upVotes;
    private long downVotes;
    private String language;
    @Ignore
    private SourceInfo sourceInfo;

    @Ignore
    public News() {

    }

    public News(long id) {
        this.id = id;
    }

/*    public News(long newsId, @NonNull String guid, long publishedOn) {
        this.newsId = newsId;
        this.guid = guid;
        this.publishedOn = publishedOn;
    }*/

    @Ignore
    private News(Parcel in) {
        super(in);
        newsId = in.readLong();
        guid = in.readString();
        publishedOn = in.readLong();
        imageUrl = in.readString();
        title = in.readString();
        url = in.readString();
        source = in.readString();
        body = in.readString();
        tags = in.readString();
        categories = in.readString();
        upVotes = in.readLong();
        downVotes = in.readLong();
        language= in.readString();
        sourceInfo = in.readParcelable(SourceInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(newsId);
        dest.writeString(guid);
        dest.writeLong(publishedOn);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(source);
        dest.writeString(body);
        dest.writeString(tags);
        dest.writeString(categories);
        dest.writeLong(upVotes);
        dest.writeLong(downVotes);
        dest.writeString(language);
        dest.writeParcelable(sourceInfo, flags);
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };


    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public void setGuid( String guid) {
        this.guid = guid;
    }

    public void setPublishedOn(long publishedOn) {
        this.publishedOn = publishedOn;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setUpVotes(long upVotes) {
        this.upVotes = upVotes;
    }

    public void setDownVotes(long downVotes) {
        this.downVotes = downVotes;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSourceInfo(SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public long getNewsId() {
        return newsId;
    }

    public String getGuid() {
        return guid;
    }

    public long getPublishedOn() {
        return publishedOn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public String getBody() {
        return body;
    }

    public String getTags() {
        return tags;
    }

    public String getCategories() {
        return categories;
    }

    public long getUpVotes() {
        return upVotes;
    }

    public long getDownVotes() {
        return downVotes;
    }

    public String getLanguage() {
        return language;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
