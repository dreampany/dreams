package com.dreampany.tools.ui.request

import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Request
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2020-02-21
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuestionRequest(
    type: Type = Type.DEFAULT,
    subtype: Subtype = Subtype.DEFAULT,
    state: State = State.DEFAULT,
    action: Action = Action.DEFAULT,
    source: Source = Source.DEFAULT,
    single: Boolean = Constants.Default.BOOLEAN,
    important: Boolean = Constants.Default.BOOLEAN,
    progress: Boolean = Constants.Default.BOOLEAN,
    limit: Long = Constants.Default.LONG,
    id: String? = Constants.Default.NULL,
    ids: List<String>? = Constants.Default.NULL,
    input: Question? = Constants.Default.NULL,
    inputs: List<Question>? = Constants.Default.NULL,
    val category: Question.Category? = Constants.Default.NULL,
    val questionType: Question.Type? = Constants.Default.NULL,
    val difficult: Difficult? = Constants.Default.NULL,
    val given: String? = Constants.Default.NULL
) : Request<Question>(
    type = type,
    subtype = subtype,
    state = state,
    action = action,
    source = source,
    single = single,
    important = important,
    progress = progress,
    limit = limit,
    id = id,
    ids = ids,
    input = input,
    inputs = inputs
) {


}