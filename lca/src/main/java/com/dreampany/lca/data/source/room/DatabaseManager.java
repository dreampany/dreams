package com.dreampany.lca.data.source.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.dreampany.lca.BuildConfig;
import com.dreampany.lca.data.model.*;
import com.dreampany.lca.data.source.dao.*;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Database(
        entities = {
                Coin.class,
                Quote.class,
                Price.class,
                Exchange.class,
                Market.class,
                Graph.class,
                Ico.class,
                News.class,
                CoinAlert.class
        },
        version = 9
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

    public abstract CoinDao coinDao();

    public abstract QuoteDao quoteDao();

    public abstract PriceDao priceDao();

    public abstract ExchangeDao exchangeDao();

    public abstract MarketDao marketDao();

    public abstract GraphDao graphDao();

    public abstract IcoDao icoDao();

    public abstract NewsDao newsDao();

    public abstract CoinAlertDao coinAlertDao();

}