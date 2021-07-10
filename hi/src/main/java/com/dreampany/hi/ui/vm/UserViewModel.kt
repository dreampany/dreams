package com.dreampany.hi.ui.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
}