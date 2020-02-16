package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.enums.Difficult
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class QuestionPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.QUESTION
    }

    fun setCategory(category: Question.Category) {
        return setPrivately(Constants.Pref.Question.CATEGORY, category)
    }

    fun getCategory(default: Question.Category) : Question.Category {
        return getPrivately(Constants.Pref.Question.CATEGORY, Question.Category::class.java, default)
    }

    fun setType(type: Question.Type) {
        return setPrivately(Constants.Pref.Question.TYPE, type)
    }

    fun getType(default: Question.Type) : Question.Type {
        return getPrivately(Constants.Pref.Question.TYPE, Question.Type::class.java, default)
    }

    fun setDifficult(difficult: Difficult) {
        return setPrivately(Constants.Pref.Question.DIFFICULT, difficult)
    }

    fun getDifficult(default: Difficult) : Difficult {
        return getPrivately(Constants.Pref.Question.DIFFICULT, Difficult::class.java, default)
    }
}