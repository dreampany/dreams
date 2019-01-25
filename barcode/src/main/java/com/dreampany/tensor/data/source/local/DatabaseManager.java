package com.dreampany.tensor.data.source.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.dreampany.tensor.BuildConfig;
import com.dreampany.tensor.data.model.Task;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

@Database(entities = {Task.class}, version = 1)
public abstract class DatabaseManager extends RoomDatabase {
    private static final String DATABASE = Iterables.getLast(Splitter.on(".").trimResults().split(BuildConfig.APPLICATION_ID)).concat("-db");
    private static volatile DatabaseManager instance;

    public abstract TaskDao taskDao();

    synchronized public static DatabaseManager onInstance(Context context) {
        if (instance == null) {
            instance = newInstance(context, false);
        }
        return instance;
    }

    public static DatabaseManager newInstance(Context context, boolean memoryOnly) {
        RoomDatabase.Builder<DatabaseManager> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), DatabaseManager.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), DatabaseManager.class, DATABASE);
        }

        return builder.fallbackToDestructiveMigration().build();
    }

}
