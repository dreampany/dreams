package com.dreampany.frame.data.source.room;

import androidx.room.TypeConverter;

import com.dreampany.frame.data.enums.Language;

/**
 * Created by Roman-372 on 5/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class LanguageConverter {
    @TypeConverter
    public static Language toLanguage(String name) {
        return Language.valueOf(name);
    }

    @TypeConverter
    public static String toName(Language language) {
        return language.name();
    }
}
