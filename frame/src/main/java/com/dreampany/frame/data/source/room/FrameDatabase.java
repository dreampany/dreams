package com.dreampany.frame.data.source.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.dreampany.frame.BuildConfig;
import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.model.Store;
import com.dreampany.frame.data.source.local.FlagDao;
import com.dreampany.frame.data.source.local.StateDao;
import com.dreampany.frame.data.source.local.StoreDao;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Database(
        entities = {
                Flag.class,
                State.class,
                Store.class
        },
        version = 5
)
public abstract class FrameDatabase extends RoomDatabase {
    private static final String DATABASE = Iterables.getLast(Splitter.on(".").trimResults().split(BuildConfig.APPLICATION_ID)).concat("-db");
    private static volatile FrameDatabase instance;

    synchronized public static FrameDatabase onInstance(Context context) {
        if (instance == null) {
            instance = newInstance(context, false);
        }
        return instance;
    }

    public static FrameDatabase newInstance(Context context, boolean memoryOnly) {
        RoomDatabase.Builder<FrameDatabase> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), FrameDatabase.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), FrameDatabase.class, DATABASE);
        }

        return builder
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract FlagDao flagDao();

    public abstract StateDao stateDao();

    public abstract StoreDao storeDao();
}
