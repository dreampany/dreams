package com.dreampany.network.misc

import com.dreampany.network.data.model.ScanResult


/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
interface ScanCallback {
    fun onResult(result: List<ScanResult>)
}