package com.dreampany.word.data.source.room;

import androidx.room.TypeConverter;

import com.dreampany.framework.util.DataUtil;
import com.dreampany.word.data.model.Definition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Hawladar Roman on 9/3/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class Converters {

    private static Gson gson = new Gson();
    private static Type typeOfListString = new TypeToken<List<String>>() {}.getType();
    private static Type typeOfListDefinitions = new TypeToken<List<Definition>>() {}.getType();

    @TypeConverter
    synchronized public static String toString(List<String> values) {
        if (DataUtil.isEmpty(values)) {
            return null;
        }
        return gson.toJson(values, typeOfListString);
    }

    @TypeConverter
    synchronized public static List<String> toList(String json) {
        if (DataUtil.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, typeOfListString);
    }


    @TypeConverter
    synchronized public static String fromDefinitions(List<Definition> definitions) {
        return gson.toJson(definitions, typeOfListDefinitions);
    }

    @TypeConverter
    synchronized public static List<Definition> fromDefinitionJson(String json) {
        return gson.fromJson(json, typeOfListDefinitions);
    }

}
