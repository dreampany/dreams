package com.dreampany.common.ui.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.dreampany.common.data.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseViewModel protected constructor(
    application: Application
) : AndroidViewModel(application) {

    protected val job: Job
    protected val uiScope: CoroutineScope

    //protected val singleOwners: MutableList<LifecycleOwner>
    //protected val multipleOwners: MutableList<LifecycleOwner>

    //protected val output: MutableLiveData<Response<T, ST, S, A, I>>
    //protected val outputs: MutableLiveData<Response<T, ST, S, A, List<I>>>

    init {
        job = SupervisorJob()
        uiScope = CoroutineScope(Dispatchers.Main + job)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    protected val context : Context
        get() = getApplication()
}