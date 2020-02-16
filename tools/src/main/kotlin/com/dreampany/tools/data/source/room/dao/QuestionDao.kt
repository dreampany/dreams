package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.question.Question

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface QuestionDao : BaseDao<Question>  {
}