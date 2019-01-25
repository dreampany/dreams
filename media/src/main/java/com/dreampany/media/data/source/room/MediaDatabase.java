package com.dreampany.media.data.source.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.dreampany.media.BuildConfig;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.model.Image;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Database(
        entities = {
                Apk.class,
                Image.class
        },
        version = 2
)
@TypeConverters(Converters.class)
public abstract class MediaDatabase extends RoomDatabase {
    private static final String DATABASE = Iterables.getLast(Splitter.on(".").trimResults().split(BuildConfig.APPLICATION_ID)).concat("-db");
    private static volatile MediaDatabase instance;

    synchronized public static MediaDatabase onInstance(Context context) {
        if (instance == null) {
            instance = newInstance(context, false);
        }
        return instance;
    }

    public static MediaDatabase newInstance(Context context, boolean memoryOnly) {
        Builder<MediaDatabase> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), MediaDatabase.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), MediaDatabase.class, DATABASE);
        }

        return builder.fallbackToDestructiveMigration().build();
    }

    public abstract ApkDao apkDao();

    public abstract ImageDao imageDao();

 /*   public abstract AudioDao audioDao();

    public abstract VideoDao videoDao();

    public abstract DocumentDao documentDao();

    public abstract FileDao fileDao();*/
}
