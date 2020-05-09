package com.dreampany.tools.api.history

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WikiHistory(val year: String,
                  val text: String,
                  val html: String,
                  val links: MutableList<WikiLink>)