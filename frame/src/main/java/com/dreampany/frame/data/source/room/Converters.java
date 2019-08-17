/*
package com.dreampany.frame.data.source.room;

import androidx.room.TypeConverter;
import com.dreampany.frame.util.DataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

*/
/**
 * Created by Hawladar Roman on 1/9/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

public class Converters {

    private static Gson gson = new Gson();
    private static Type typeOfListString = new TypeToken<List<String>>() {}.getType();

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
}
*/
