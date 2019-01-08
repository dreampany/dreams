package com.dreampany.scan.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.dreampany.scan.BuildConfig;
import com.dreampany.scan.data.model.Scan;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Created by Hawladar Roman on 7/2/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Database(
        entities = {
                Scan.class
        },
        version = 1
)
@TypeConverters(Converters.class)
public abstract class DatabaseManager extends RoomDatabase {
    private static final String DATABASE = Iterables.getLast(Splitter.on(".").trimResults().split(BuildConfig.APPLICATION_ID)).concat("-db");
    private static volatile DatabaseManager instance;

    public static DatabaseManager onInstance(Context context) {
        if (instance == null) {
            instance = newInstance(context, false);
        }
        return instance;
    }

    synchronized public static DatabaseManager newInstance(Context context, boolean memoryOnly) {
        RoomDatabase.Builder<DatabaseManager> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), DatabaseManager.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), DatabaseManager.class, DATABASE);
        }

        return builder.fallbackToDestructiveMigration().build();
    }

    public abstract ScanDao scanDao();
}
