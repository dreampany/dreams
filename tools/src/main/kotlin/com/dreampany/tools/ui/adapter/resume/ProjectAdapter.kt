package com.dreampany.tools.ui.adapter.resume

import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.tools.ui.model.resume.ProjectItem
import com.dreampany.tools.ui.model.resume.SchoolItem

/**
 * Created by roman on 2020-01-15
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ProjectAdapter(listener: Any? = null) : SmartAdapter<ProjectItem>(listener) {

    companion object {
        private val SPAN_COUNT = 2
        private val ITEM_OFFSET = 4
    }


    fun getSpanCount(): Int {
        return SPAN_COUNT
    }

    fun getItemOffset(): Int {
        return ITEM_OFFSET
    }
}