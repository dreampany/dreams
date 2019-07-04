package com.dreampany.frame.data.source.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.dreampany.frame.BuildConfig;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.model.Store;
import com.dreampany.frame.data.source.StateDao;
import com.dreampany.frame.data.source.StoreDao;
import com.dreampany.frame.misc.Constants;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Database(
        entities = {
                State.class,
                Store.class
        },
        version = 8
)
public abstract class FrameDatabase extends RoomDatabase {
    private static final String DATABASE = Iterables.getLast(Splitter.on(Constants.Sep.DOT).trimResults().split(BuildConfig.APPLICATION_ID)).concat("-db");
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

    public abstract StateDao stateDao();

    public abstract StoreDao storeDao();
}
