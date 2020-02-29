package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.framework.data.source.api.DataSourceRx
import com.dreampany.tools.data.model.resume.Resume

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ResumeDataSource : DataSource<Resume>, DataSourceRx<Resume> {
}