package com.dreampany.lca.data.source.room;

import androidx.room.TypeConverter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.model.Quote;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 15/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class Converters {
    private static Gson gson = new Gson();
    private static Type typeOfListString = new TypeToken<List<String>>() {}.getType();
    private static Type typeOfMap = new TypeToken<Map<Currency, Quote>>() {
    }.getType();
    private static Type typeOfListPrices = new TypeToken<List<List<Float>>>() {
    }.getType();

    @TypeConverter
    synchronized public static String toString(List<String> values) {
        if (DataUtil.isEmpty(values)) {
            return null;
        }
        return gson.toJson(values, typeOfListString);
    }

    @TypeConverter
    synchronized  public static List<String> toList(String json) {
        if (DataUtil.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, typeOfListString);
    }

    @TypeConverter
    synchronized public static String fromPriceQuotes(Map<Currency, Quote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return null;
        }
        return gson.toJson(quotes, typeOfMap);
    }

    @TypeConverter
    synchronized public static Map<Currency, Quote> fromMapString(String json) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, typeOfMap);
    }

    @TypeConverter
    synchronized public static String fromListOfListPrice(List<List<Float>> prices) {
        if (prices == null || prices.isEmpty()) {
            return null;
        }

        return gson.toJson(prices, typeOfListPrices);
    }

    @TypeConverter
    synchronized public static List<List<Float>> fromListOfListPricesString(String json) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, typeOfListPrices);
    }

    @TypeConverter
    synchronized public static String toString(Currency currency) {
        if (currency == null) {
            return null;
        }
        return currency.toString();
    }

    @TypeConverter
    synchronized  public static Currency toCurrency(String currency) {
        if (DataUtil.isEmpty(currency)) {
            return null;
        }
        return Currency.valueOf(currency);
    }

    @TypeConverter
    synchronized public static String toString(CoinSource source) {
        if (source == null) {
            return null;
        }
        return source.toString();
    }

    @TypeConverter
    synchronized  public static CoinSource toCoinSource(String source) {
        if (DataUtil.isEmpty(source)) {
            return null;
        }
        return CoinSource.valueOf(source);
    }
}
