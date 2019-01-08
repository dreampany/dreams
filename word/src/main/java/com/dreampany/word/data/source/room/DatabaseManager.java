package com.dreampany.word.data.source.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.dreampany.word.BuildConfig;
import com.dreampany.word.data.model.Antonym;
import com.dreampany.word.data.model.Synonym;
import com.dreampany.word.data.model.Word;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Database(
        entities = {
                Word.class,
                Synonym.class,
                Antonym.class
        },
        version = 3
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
        Builder<DatabaseManager> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), DatabaseManager.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), DatabaseManager.class, DATABASE);
        }

        return builder.fallbackToDestructiveMigration().build();
    }

    public abstract WordDao wordDao();

    public abstract SynonymDao synonymDao();

    public abstract AntonymDao antonymDao();

}