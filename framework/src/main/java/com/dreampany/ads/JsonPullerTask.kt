package com.dreampany.ads

import android.os.AsyncTask
import android.util.Log
import androidx.annotation.RestrictTo

/**
 * Created by roman on 16/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class JsonPullerTask(private val jsonUrl: String, private val listener: JsonPullerListener) : AsyncTask<String, String, String>() {

    override fun doInBackground(vararg p1: String): String {
        return HouseAdsHelper.parseJsonObject(jsonUrl)
    }

    override fun onPostExecute(result: String) {
        listener.onPostExecute(result)
        Log.d("Response", result)
    }

    interface JsonPullerListener {
        fun onPostExecute(result: String)
    }
}