package com.dreampany.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


/**
 * Created by Roman-372 on 7/1/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ConnectionUtil {

   companion object {
       fun getNetworkInfo(context: Context): NetworkInfo? {
           val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
           return cm.activeNetworkInfo
       }
   }
}