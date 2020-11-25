package com.dreampany.tools.misc.exts

import com.dreampany.tools.R

/**
 * Created by roman on 11/25/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val Boolean.favoriteIcon: Int get() = if (this) R.drawable.ic_favorite_red_24dp else R.drawable.ic_favorite_black_24dp