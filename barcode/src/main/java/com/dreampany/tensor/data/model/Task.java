package com.dreampany.tensor.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dreampany.framework.data.model.Base;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

@Entity
public class Task extends Base {

    @PrimaryKey
    @NonNull
    private final String id;
    @Nullable
    private String title;
    @Nullable
    private String description;
    private boolean completed;

    @Ignore
    public Task() {
        this((String) null);
    }

    @Ignore
    public Task(@Nullable String title) {
        this(title, null);
    }

    @Ignore
    public Task(@Nullable String title, @Nullable String description) {
        this(UUID.randomUUID().toString(), title, null);
    }

    public Task(@NonNull String id, @Nullable String title, @Nullable String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Ignore
    private Task(Parcel in) {
        super(in);
        id = in.readString();
        title = in.readString();
        description = in.readString();
        completed = in.readByte() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (completed ? 1 : 0));
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) {
            return true;
        }
        if (inObject == null || getClass() != inObject.getClass()) {
            return false;
        }
        Task task = (Task) inObject;
        return Objects.equal(id, task.id) &&
                Objects.equal(title, task.title) &&
                Objects.equal(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, description);
    }

    @Override
    public String toString() {
        return "Task: " + title;
    }

    public Task setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public Task setDescription(@Nullable String description) {
        this.description = description;
        return this;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(title)) {
            return title;
        } else {
            return description;
        }
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isActive() {
        return !completed;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) && Strings.isNullOrEmpty(description);
    }


}
