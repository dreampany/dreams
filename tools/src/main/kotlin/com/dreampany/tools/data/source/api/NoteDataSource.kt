package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.framework.data.source.api.DataSourceRx
import com.dreampany.tools.data.model.Note

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface NoteDataSource : DataSource<Note>, DataSourceRx<Note> {
}