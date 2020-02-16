package com.dreampany.tools.data.source.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.enums.Difficult
import com.dreampany.framework.data.source.room.converters.Converters
import com.dreampany.tools.data.model.question.Question

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuestionConverters: Converters() {

    @Synchronized
    @TypeConverter
    fun toValue(category: Question.Category?): String? {
        return category?.name
    }

    @Synchronized
    @TypeConverter
    fun toCategory(value: String?): Question.Category? {
        return if (value.isNullOrEmpty()) null else Question.Category.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toValue(type: Question.Type?): String? {
        return type?.name
    }

    @Synchronized
    @TypeConverter
    fun toQuestionType(value: String?): Question.Type? {
        return if (value.isNullOrEmpty()) null else Question.Type.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toValue(difficult: Difficult?): String? {
        return difficult?.name
    }

    @Synchronized
    @TypeConverter
    fun toDifficult(value: String?): Difficult? {
        return if (value.isNullOrEmpty()) null else Difficult.valueOf(value)
    }
}