package com.dreampany.radio.data.source.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import com.dreampany.frame.data.source.room.Converters;
import com.dreampany.radio.BuildConfig;
import com.dreampany.radio.data.model.Station;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Created by Hawladar Roman on 1/9/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Database(
        entities = {
                Station.class
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

    public abstract StationDao stationDao();
}
