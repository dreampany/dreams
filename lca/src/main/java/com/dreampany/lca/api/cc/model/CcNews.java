package com.dreampany.lca.api.cc.model;

import com.dreampany.frame.util.DataUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CcNews {

    public static class SourceInfo {
        private final String name;
        private final String language;
        private final String imageUrl;

        @JsonCreator
        public SourceInfo(@JsonProperty("name") String name,
                          @JsonProperty("lang") String language,
                          @JsonProperty("img") String imageUrl) {
            this.name = name;
            this.language = language;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getLanguage() {
            return language;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    private final long id;
    private final String guid;
    private final long publishedOn;
    private final String imageUrl;
    private final String title;
    private final String url;
    private final String source;
    private final String body;
    private final String tags;
    private final String categories;
    private final long upVotes;
    private final long downVotes;
    private final String language;
    private final SourceInfo sourceInfo;

    @JsonCreator
    public CcNews(@JsonProperty("id") long id,
                  @JsonProperty("guid") String guid,
                  @JsonProperty("published_on") long publishedOn,
                  @JsonProperty("imageurl") String imageUrl,
                  @JsonProperty("title") String title,
                  @JsonProperty("url") String url,
                  @JsonProperty("source") String source,
                  @JsonProperty("body") String body,
                  @JsonProperty("tags") String tags,
                  @JsonProperty("categories") String categories,
                  @JsonProperty("upvotes") long upVotes,
                  @JsonProperty("downvotes") long downVotes,
                  @JsonProperty("lang") String language,
                  @JsonProperty("source_info") SourceInfo sourceInfo) {
        this.id = id;
        this.guid = guid;
        this.publishedOn = publishedOn;
        this.imageUrl = imageUrl;
        this.title = title;
        this.url = url;
        this.source = source;
        this.body = body;
        this.tags = tags;
        this.categories = categories;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.language = language;
        this.sourceInfo = sourceInfo;
    }

    public long getId() {
        return id;
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

    public long getHash() {
        return DataUtil.getSha512(guid);
    }
}
